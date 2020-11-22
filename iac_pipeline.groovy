// Declarative pipeline
pipeline {
  agent any
  stages {
    stage('Submit Stack') { 
      steps {
        //sh "aws cloudformation create-stack --stack-name myteststack --template-body file:///home/testuser/mytemplate.json --region 'us-east-1' --parameters ParameterKey=Parm1,ParameterValue=test1 ParameterKey=Parm2,ParameterValue=test2"
        //sh "aws cloudformation create-stack --stack-name sundayWebStack --template-body  $workspace/cloudformation/sunday_web_stack.json  --region 'us-east-1' --parameters ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=KeyName,ParameterValue='s3://cf-templates-a1jqt245fpux-us-east-1/DevOpsKeyPair.pem' ParameterKey=SSHLocation,ParameterValue=0.0.0.0/0"
         sh "aws cloudformation validate-template --template-body  $workspace/cloudformation/sunday_web_stack.json" 
      }
         }
  }
}