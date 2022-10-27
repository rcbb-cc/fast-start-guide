package cc.rcbb.gradle.demo.docker

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildImagePlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.task("testBuildImage") {
			println("================testBuildImage=============")
			group "docker"
			doLast {
				println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
			}
		}
	}
}
