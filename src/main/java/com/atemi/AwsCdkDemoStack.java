package com.atemi;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ClusterProps;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.EcrImage;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.FargateTaskDefinitionProps;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.services.ecs.RepositoryImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateServiceProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.iam.Role;

public class AwsCdkDemoStack extends Stack {

    public AwsCdkDemoStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsCdkDemoStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        Vpc vpc = new Vpc(this, "CdkDemoVpc",
                VpcProps.builder().maxAzs(3).build());

        Cluster cluster = new Cluster(this, "CdkDemoCluster",
                ClusterProps.builder().vpc(vpc).build());

        FargateTaskDefinition taskDef = new FargateTaskDefinition(this, "CdkDemoTaskDefinition",
                FargateTaskDefinitionProps.builder()
                        .executionRole(Role.fromRoleArn(this, "CdkDemoRole",
                                "arn:aws:iam::" + this.getAccount() + ":role/ecsTaskExecutionRole"))
                        .build());
        RepositoryImage image = EcrImage.fromRegistry(
                this.getAccount() + ".dkr.ecr." + this.getRegion()
                        + ".amazonaws.com/aws-cdk-demo");
        ContainerDefinitionOptions containerOpts = ContainerDefinitionOptions.builder()
                //.image(ContainerImage.fromEcrRepository(new Repository(this, "cdk-demo")))
                .image(image)
                .build();
        taskDef
                .addContainer("cdkContainer", containerOpts)
                .addPortMappings(PortMapping.builder()
                        .hostPort(8080)
                        .containerPort(8080)
                        .build());

        ApplicationLoadBalancedFargateServiceProps serviceProps = ApplicationLoadBalancedFargateServiceProps.builder()
                .cluster(cluster)
                .desiredCount(3)
                .memoryLimitMiB(2048)
                .listenerPort(8080)
                .taskDefinition(taskDef)
                .build();

        ApplicationLoadBalancedFargateService service = new ApplicationLoadBalancedFargateService(
                this, "CdkDemoService", serviceProps);

        service.getTargetGroup().setHealthCheck(HealthCheck.builder()
                .path("/")
                .port("8080")
                .build());

    }
}
