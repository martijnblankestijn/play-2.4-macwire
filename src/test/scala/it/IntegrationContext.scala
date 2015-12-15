package it

import modules.CourseApplicationLoader
import play.api.test.WithApplicationLoader

class IntegrationContext extends WithApplicationLoader(new CourseApplicationLoader) {

}
