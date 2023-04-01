# SpringBoot使用Hibernate Validator（后端参数校验）

1. 表单提交，前端做的了参数校验，服务器端还需要做参数校验吗？<br/>
2. 服务器端怎么做参数校验？怎么样做参数校验最快捷？<br/>
3. JSR-303 是什么？如何使用？SpringBoot下如何使用？<br/>
4. 使用一个实体接收提交内容时，新增和更新操作的参数校验标准不同时如何解决？<br/>
5. 如何自定义校验？<br/>

<!-- more -->

SpringBoot 使用 JSR303 实现参数验证。    
SpringBoot 使用 Hibernate Validator 校验。  
示例代码，下文所有源代码都在这个仓库。

[Validator-demo Github地址](https://github.com/rcbb-cc/fast-start-guide)

## JSR-303

看 JSR-303，首先我们得看看 JSR 是什么含义。  
是指向 JCP(Java Community Process) 提出新增一个标准化技术规范的正式请求。任何人都可以提交JSR，以向Java平台增添新的API和服务。JSR已成为Java界的一个重要标准。
JSR，Java Specification Requests 的缩写，意思是 Java 规范提案。  
**JSR-303 是JAVA EE 6 中的一项子规范**，叫做 Bean Validation。

![JSR303-规范](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2020/11/JSR303-%E8%A7%84%E8%8C%83_1605698683634.png?x-oss-process=style/yuantu_shuiyin)

Hibernate Validator 是 Bean Validation 的参考实现，Hibernate Validator 提供了 JSR 303 规范中所有内置 constraint 的实现，除此之外还有一些附加的 constraint。  
![HibernateValidator附加的](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2020/11/HibernateValidator%E9%99%84%E5%8A%A0%E7%9A%84_1605698765125.png?x-oss-process=style/yuantu_shuiyin)


## SpringBoot使用JSR-303

SpringBoot 使用 JSR303 实现参数验证。  
SpringBoot 使用 Hibernate Validator 实现参数验证。

## 基本使用

1. 在接收参数的 DTO 上加上相关验证条件；
~~~
@Data
public class TestDTO1 {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @NotNull
    private Integer status;
}
~~~
2. 开启校验功能 @Valid；
~~~
@PostMapping("/test1")
public R test1(@Valid @RequestBody TestDTO1 dto) {
    log.info("[TestDTO1 <{}>]", dto);
    return R.ok();
}
~~~
3. 给校验的 bean 后紧跟一个 BindingResult，就可以获取到校验的结果；
~~~
@PostMapping("/test2")
public R test2(@Valid @RequestBody TestDTO1 dto, BindingResult result) {
    log.info("[TestDTO1 <{}>]", dto);
    log.error("[BindingResult <{}>]", result);
    if (result != null && result.getErrorCount() > 0) {
        return R.failed(result.getAllErrors());
    }
    return R.ok();
}
~~~
## 全局异常处理

加上全局异常处理
~~~
@Slf4j
@RestControllerAdvice
public class GlobalBizExceptionHandler {

    /**
     * 全局异常.
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.failed(e.getLocalizedMessage());
    }
}
~~~
使用 @RequestBody 来接收参数时，当传入的参数不符合要求时，抛出的异常为 org.springframework.web.bind.MethodArgumentNotValidException，所以我们对这个异常进行处理。

~~~
/**
 * validation Exception
 * @param exception
 * @return R
 */
@ExceptionHandler({ MethodArgumentNotValidException.class })
@ResponseStatus(HttpStatus.BAD_REQUEST)
public R handleBodyValidException(MethodArgumentNotValidException exception) {
    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    log.warn("[参数绑定异常,ex = {}]", fieldErrors.get(0).getDefaultMessage());
    return R.failed(fieldErrors.get(0).getDefaultMessage());
}
~~~

使用以 form-data 形式传参时，当传入的参数不符合要求时，抛出的异常为 org.springframework.validation.BindException，所以我们对这个异常进行处理。
~~~
/**
 * validation Exception (以form-data形式传参)
 * @param exception
 * @return R
 */
@ExceptionHandler({ BindException.class })
@ResponseStatus(HttpStatus.BAD_REQUEST)
public R bindExceptionHandler(BindException exception) {
    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    log.warn("[参数绑定异常,ex = {}]", fieldErrors.get(0).getDefaultMessage());
    return R.failed(fieldErrors.get(0).getDefaultMessage());
}
~~~

加上全局异常捕获后，参数若有不符合，请求的结果如下：
~~~
{
    "code": 1,
    "msg": "id必须为空",
    "data": null
}
{
    "code": 1,
    "msg": "age不能为空",
    "data": null
}
~~~

## 分组校验

使用一个实体接收提交内容时，新增和更新操作的参数校验标准不同时如何解决？  
例：新增操作不需要传入 id，更新操作必须传入 id。  
上面说的问题都是最基础的，那如何实现呢？
1. 创建分组标识。
~~~
public interface AddGroup {}
public interface UpdateGroup {}
~~~
2. 在接收参数的 DTO 上标识，给校验注解标注什么情况需要进行校验。
~~~
@NotNull(message = "id不能为空", groups = {UpdateGroup.class})
@Null(message = "id必须为空", groups = {AddGroup.class})
private Long id;
~~~
3. 在 Controller 的方法上，使用 @Validated 指定分组标识。
~~~
@PostMapping("/test3")
public R test3(@Validated(AddGroup.class) @RequestBody TestDTO3 dto) {
    log.info("[TestDTO3 <{}>]", dto);
    return R.ok();
}
~~~

**注意点：** 默认没有指定分组的校验注解，在分组校验情况下不生效。  
（例：我们有个参数的校验规则没有分组， @NotBlank 没有进行分组，它无论在 AddGroup 或 UpdateGroup 下都是不生效的）

## 顺序校验
在一些场景下，参数校验是需要有顺序的。  
JSR-303 校验中专门提供了 @GroupSequence 来实现。
~~~
@GroupSequence(value = {ValidateSequence.Validate1.class,
        ValidateSequence.Validate2.class,
        ValidateSequence.Validate3.class,
        ValidateSequence.Validate4.class,
        ValidateSequence.Default.class,})
public interface ValidateSequence {
    interface Validate1{}
    interface Validate2{}
    interface Validate3{}
    interface Validate4{}
    interface Default{}
}
~~~
~~~
@Data
public class TestDTO4 {
    @NotNull(message = "id不能为空", groups = {ValidateSequence.Validate1.class})
    private Long id;
    @NotBlank(message = "name不能为空", groups = {  ValidateSequence.Validate2.class})
    private String name;
    @NotNull(message = "age不能为空", groups = {  ValidateSequence.Validate2.class})
    private Integer age;
    @NotNull(message = "status不能为空")
    private Integer status;
}
~~~
~~~
@PostMapping("/test5")
public R test5(@Validated(ValidateSequence.class) @RequestBody TestDTO4 dto) {
    log.info("[TestDTO4 <{}>]", dto);
    return R.ok();
}
~~~
## 自定义校验
有些时候，我们需要的一些校验规则，库中没有提供，这时候我们就需要自定义了。

一些简单的，可以通过**正则表达式匹配**可以实现。
~~~
@Data
public class TestDTO5 {
    @NotNull(message = "id不能为空")
    private Long id;
    @NotBlank(message = "name不能为空")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,17}$", message = "用户名由字母开头的6-18位数字和字母组成")
    private String name;
    @NotBlank(message = "phone不能为空")
    @Pattern(regexp = "^1[3|4|5|6|8|7|9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
~~~
但是有一些，正则表达式无法判断。   
例：传入的是一个 List，但我们要限制传入的值，只能是固定的一些数。    
1.编写一个自定义的校验注解；
~~~
@Documented
// 可以指定多个不同的校验器
@Constraint(validatedBy = {ListInValuesValiadator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ListInValues {
    // 默认提示值可自定义，词条文件：ValidationMessages.properties
    String message() default "{javax.validation.constraints.NotBlank.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int[] values() default {};
}
~~~
2.编写一个自定义的校验器；
~~~
public class ListInValuesValiadator implements ConstraintValidator<ListInValues, Integer> {
    private Set<Integer> values = new HashSet<>();
    @Override
    public void initialize(ListInValues constraintAnnotation) {
        // 初始化
        int[] vals = constraintAnnotation.values();
        for (int val : vals) {
            values.add(val);
        }
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 校验
        return values.contains(value);
    }
}
~~~
3.关联自定义的校验器和自定义的校验注解，@Constraint(validatedBy = {ListInValuesValiadator.class})；  
4.使用；
~~~
@NotNull
@ListInValues(values = {0,1}, message = "错误值")
private Integer status;
~~~

![校验不通过返回](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2020/11/%E6%A0%A1%E9%AA%8C%E4%B8%8D%E9%80%9A%E8%BF%87%E8%BF%94%E5%9B%9E_1605779244452.png?x-oss-process=style/yuantu_shuiyin)


## 源码

[Validator-demo Github地址](https://github.com/rcbb-cc/fast-start-guide)

更多好文，请关注[我的博客](https://rcbb.cc)
