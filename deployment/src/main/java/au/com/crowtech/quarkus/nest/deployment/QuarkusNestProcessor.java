package au.com.crowtech.quarkus.nest.deployment;

import au.com.crowtech.quarkus.nest.GreetingServlet;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem;

class QuarkusNestProcessor {

    private static final String FEATURE = "quarkus-nest";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
    
    @BuildStep
    ServletBuildItem createServlet() { 
      ServletBuildItem servletBuildItem = ServletBuildItem.builder("crowtech-nest", GreetingServlet.class.getName())
        .addMapping("/greeting")
        .build(); 
      return servletBuildItem;
    }

}
