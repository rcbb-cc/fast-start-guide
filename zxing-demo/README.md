## 使用场景

使用场景：H5 或 APP 上需要根据服务器的某些数据来动态生成二维码的时候。

## 具体实现

### 依赖

主要是利用 zxing 库，google 的开源库。

> ZXing是一个开放源码的，用Java实现的多种格式的1D/2D条码图像处理库，它包含了联系到其他语言的端口。Zxing可以实现使用手机的内置的摄像头完成条形码的扫描及解码。

<!-- more -->

```
<!-- 二维码生成 -->
<dependency>
	<groupId>com.google.zxing</groupId>
	<artifactId>core</artifactId>
	<version>3.3.3</version>
</dependency>
<dependency>
	<groupId>com.google.zxing</groupId>
	<artifactId>javase</artifactId>
	<version>3.3.3</version>
</dependency>
```

### 工具类

```
/**
 * <p>
 * QRCodeUtil
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/28
 */
@Slf4j
@UtilityClass
public class QRCodeUtil {

    /**
     * 默认宽度
     */
    private static final Integer WIDTH = 140;
    /**
     * 默认高度
     */
    private static final Integer HEIGHT = 140;

    /**
     * LOGO 默认宽度
     */
    private static final Integer LOGO_WIDTH = 22;
    /**
     * LOGO 默认高度
     */
    private static final Integer LOGO_HEIGHT = 22;

    /**
     * 图片格式
     */
    private static final String IMAGE_FORMAT = "png";
    private static final String CHARSET = "utf-8";
    /**
     * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析
     */
    private static final String BASE64_IMAGE = "data:image/png;base64,%s";

    /**
     * 生成二维码，使用默认尺寸
     *
     * @param content 内容
     * @return
     */
    public String getBase64QRCode(String content) {
        return getBase64Image(content, WIDTH, HEIGHT, null, null, null);
    }

    /**
     * 生成二维码，使用默认尺寸二维码，插入默认尺寸logo
     *
     * @param content 内容
     * @param logoUrl logo地址
     * @return
     */
    public String getBase64QRCode(String content, String logoUrl) {
        return getBase64Image(content, WIDTH, HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    public String getBase64QRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        return getBase64Image(content, width, height, logoUrl, logoWidth, logoHeight);
    }

    private String getBase64Image(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = crateQRCode(content, width, height, logoUrl, logoWidth, logoHeight);
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
        } catch (IOException e) {
            log.error("[生成二维码，错误{}]", e);
        }
        // 转出即可直接使用
        return String.format(BASE64_IMAGE, Base64.encode(os.toByteArray()));
    }


    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    private BufferedImage crateQRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        if (StrUtil.isNotBlank(content)) {
            ServletOutputStream stream = null;
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
            // 指定字符编码为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                if (StrUtil.isNotBlank(logoUrl)) {
                    insertLogo(bufferedImage, width, height, logoUrl, logoWidth, logoHeight);
                }
                return bufferedImage;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 二维码插入logo
     *
     * @param source     二维码
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @throws Exception
     */
    private void insertLogo(BufferedImage source, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) throws Exception {
        // logo 源可为 File/InputStream/URL
        Image src = ImageIO.read(new URL(logoUrl));
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        graph.drawImage(src, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws IOException
     */
    public void getQRCode(String content, OutputStream output) throws IOException {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, null, null, null);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

    /**
     * 获取二维码
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     * @throws Exception
     */
    public void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

}

```


### 测试入口

```
/**
 * <p>
 * QRCodeController
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/28
 */
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    /**
     * 根据 content 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    @GetMapping("/getQRCodeBase64")
    public R getQRCode(@RequestParam("content") String content,
                       @RequestParam(value = "logoUrl", required = false) String logoUrl,
                       @RequestParam(value = "width", required = false) Integer width,
                       @RequestParam(value = "height", required = false) Integer height) {
        return R.ok(QRCodeUtil.getBase64QRCode(content, logoUrl));
    }

    /**
     * 根据 content 生成二维码
     */
    @GetMapping(value = "/getQRCode")
    public void getQRCode(HttpServletResponse response,
                          @RequestParam("content") String content,
                          @RequestParam(value = "logoUrl", required = false) String logoUrl) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRCodeUtil.getQRCode(content, logoUrl, stream);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }
}

```

### 效果

直接将图片资源以 Base64 的形式返回给了前端。

测试请求：
```
http://localhost:8080/qrcode/getQRCodeBase64?content=www.baidu.com

{
  "code": 0,
  "msg": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIwAAACMCAIAAAAhotZpAAABuklEQVR42u3aQY7EMAgEwPz/0zs/WGk0NGCn+hwlscsHZHj+ZH0eWwBJIEESSAIJkkASSJAEEiSBJJAgCSQpRnq68tV3/39454ogQYIECRKk9yFVljF1+9725oa9ggQJEiRIkO5FKqzfcgXbkhVBggQJEiRIkIaWlOOHBAkSJEiQIJ1f3eU6VZAgQYIECRKk8/tJud/Q9IMECRIkSJC6kKYmWKcufowZQ4IECRIkSAcmsR1LVwoJEiRIkCAdjFQ4SZqrlHLDsL8sEBIkSJAgQXo9Um7stPBVOf6GIhMSJEiQIEE6Gamwjlqyd7mzsqWfBAkSJEiQIC1Fyu1O4YfaHoYECRIkSJAgnbB3uXq1/1WQIEGCBAnSRUhtydVR4/UbJEiQIEGCdC/S05Xcd8dbRJAgQYIECdJbkdruY3KXRm1HBxIkSJAgQYIUW3Db4cidlaXVHSRIkCBBgvQ6pLYBmPlGGiRIkCBBggSpuDSaujRq4IcECRIkSJDuRcrdx+QaOTsHYCBBggQJEqQbkaYmWKf++chrIUiQIEGCBGkMSSAJJEgCCZJAEkiQBJJAgiSQBBIkgQRJIMk3+QBWsB+4yRwtkgAAAABJRU5ErkJggg==",
  "data": null
}
```

![二维码生成](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220828225508-334a39.png?x-oss-process=style/yuantu_shuiyin)


带 Logo 的二维码，可以传入 logo 的 url，生成的二维码中间会带有 logo。

```
http://localhost:8080/qrcode/getQRCodeBase64?content=www.baidu.com&logoUrl=https://www.baidu.com/img/flexible/logo/pc/result.png

{
    "code": 0,
    "msg": null,
    "data": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIwAAACMCAIAAAAhotZpAAADKElEQVR42u3bP2sUQRgH4OxeSG1hYWMhSNRgISq2gmBlIVhYWOoHEGuxMI1NCMHOwkL8Cn4EK8HOMginYKEQQghYJKgbF4blchcS3flzd8+PZUlg2buZZ2/v3Zm5hd9SfBZ0ASSBBEkgCSRIAkkgQRJIkASSQIIkkKRnpIVUOdHrHn1wmS2CBAkSJEiQ5g+pzzKmv35PduYEfQUJEiRIkCDNLlKP9Vu8gq2QFkGCBAkSJEiQMjUpHj8kSJAgQYIEafqru3gzVZAgQYIECRKk6Z9Pivc2TPpBggQJEiRIqZByrWDNNfBjmTEkSJAgQYI0hYnRHYW2FBIkSJAgQZpipB5XksarlOIthv2fBs4RUlGDhJBi3ccgRUQqcLgd0sRGjn3dS1eHN25+bfbN5jupt/fR75xQy9Nuk17oOK3o8bIrpbqDBAnSDCG1NmfOrjb785ffjzhBKgWpe6p5R+rx2b7HBh9GOv5J4q1pKWU+qSikC1c+nTp9H1JxSHubmwEpPCSNRRou1ZDSIXW7e+vJ47FIzTFhezSo2j8g5UHaebnRnurcxXdrG1vhM3RvUDXb56X6dl01SA8Hldtd9F+GdA9okJp/1xfrO3V192/vh4qu+5y0+/bNzvpasz/8SfrnS2c6hoXKQWr7/fliPQlp5HY3qQSHFBGpdWqRAsyHjz+b/eqLre7BPknZkK7XVRfp1evtgNSa7Q+HzdYiLdcHG6SkSLfqqnu7C3e5LtIR1R2kflp4NFL4WgpI33/sHzw27f0KSOHgpgKcVH+nLPYgDUcGILoHbz97mmtsfn6RmjwYjLndhW3l2pfu7e7byjKkREhpFtRBgpQDKVnirVHNXr/NLFKyuUdIkCBBglQuUq5fmOZarpz9NzaQIEGCBAnSDCHlevLvsSuTXTqQIEGCBAkSpGgNTnZxxLtWCq3uIEGCBAkSpLlDSrYAJv9EGiRIkCBBggSp59Io16BRAn5IkCBBggRpdpHijcfEm8gpcwEMJEiQIEGCNItIuVaw5nrPUzksBAkSJEiQIGVDEkgCCZJAgiSQBBIkgSSQIAkkgQRJIEESSHKS/AGHCLnTQ9OTzwAAAABJRU5ErkJggg=="
}
```

![带Logo的二维码](https://rcbb-blog.oss-cn-guangzhou.aliyuncs.com/2022/08/20220828230428-21e989.png?x-oss-process=style/yuantu_shuiyin)

也可以选择使用返回流的方式给前端。


## 源码

[zxing-demo Github地址](https://github.com/rcbb-cc/fast-start-guide)

更多好文，请关注[我的博客](https://rcbb.cc)