package com.atemi;

import software.amazon.awscdk.core.App;

public class AwsCdkDemoApp {
    public static void main(final String[] args) {
        App app = new App();

        new AwsCdkDemoStack(app, "AwsCdkDemoStack");

        app.synth();
    }
}
