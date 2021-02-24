//Description pipeline
pipeline {
  agent any
  stages {
    stage('Submit Stack') { 
      steps {
          catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'Jenkins-server', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']]) {
              //sh "aws cloudformation deploy --template-file  $workspace/cloudformation/TrainingEvent-UbuntuServer.json --stack-name TomCatWeb-Stack-Test --region us-east-1 --parameter-overrides InstanceType=t2.micro KeyName=myTestKeyPair02 SSHLocation=0.0.0.0/0 --tags name=TomCatWeb-Stack-Test"
              //sh "aws cloudformation create-stack --template-body '$workspace/cloudformation/TrainingEvent-UbuntuServer.py' --stack-name TomCatWeb-Stack-Val --region us-east-1 --parameters ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=KeyName,ParameterValue='myTestKeyPair02' ParameterKey=SSHLocation,ParameterValue=0.0.0.0/0"
              sh "echo SKIPPING INFRASTRUCTURE CREATION/UPDATE for now .."
            }//end withCredentials
            sh "exit 0"
         }//end catcherror
      }
    }
    stage('Update Inventory'){
      steps{
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
           //sh "echo SKIPPING Automated update of inventory file in lieu to manual update .."
             withCredentials([sshUserPrivateKey(credentialsId: 'a59a13e3-8e2f-4920-83c9-a49b576e5d58', keyFileVariable: 'myTestKeyPair02')]) {
             sh 'sudo chmod +x $workspace/inventory/ec2.py' 
             sh 'sudo chmod +x $workspace/inventory/ec2.ini'
             sh 'ansible -i $workspace/inventory/ec2.py  tag_Name_tomcat-node* --list'
            //sh 'ansible-playbook ./ansible/playbooks/update_inventory.yml --user ubuntu --key-file ${myTestKeyPair02}'  
            // sh '/etc/ansible/ec2.py --list'
           }//end withCredentials
          sh "exit 0"
         }//end catchError
      }
    }
    stage('Configure Tomcat') {
      steps {
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
          withCredentials([sshUserPrivateKey(credentialsId: 'a59a13e3-8e2f-4920-83c9-a49b576e5d58', keyFileVariable: 'myKEY')]) {
             //sh 'ansible-playbook ./ansible/playbooks/tomcat-setup.yml --user ubuntu --key-file ${myKEY}'  
           }//end withCredentials
          sh "exit 0"
         }//end catchError
      }//end steps
    } //end stage
  } //end stages
}//end pipeline