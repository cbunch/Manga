package de.herrlock.manga.cli.options;

import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public final class TestViewpageOptions {

    @Test
    public void testOptionCount() {
        ViewpageOptions mainOptions = new ViewpageOptions();
        Options options = mainOptions.getOptions();
        Assert.assertEquals( 4, options.getOptions().size() );
    }

    @Test
    public void testRequiredOptionCount() {
        ViewpageOptions mainOptions = new ViewpageOptions();
        Options options = mainOptions.getOptions();
        Assert.assertEquals( 1, options.getRequiredOptions().size() );
    }

}