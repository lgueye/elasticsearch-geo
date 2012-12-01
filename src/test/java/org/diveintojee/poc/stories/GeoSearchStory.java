/**
 *
 */
package org.diveintojee.poc.stories;

import org.diveintojee.poc.steps.GeoSearchSteps;
import org.diveintojee.poc.steps.Exchange;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class GeoSearchStory extends AbstractJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new GeoSearchSteps(new Exchange()));
    }

    @Override
    protected List<String> storyPaths() {
        List<String> paths = new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(getClass()).getFile(),
                Arrays.asList("**/geo_search.story"), null);
        return paths;
    }
}
