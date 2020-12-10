// Declarative pipeline
pipeline {
  agent any
  stages {
    stage('Submit Stack') { 
      steps {
        sh "aws cloudformation deploy --template-file  '$workspace/cloudformation/TrainingEvent-UbuntuServer.json' --stack-name TomCatWeb-Stack-Val --region 'us-east-1' --parameter-overrides InstanceType=t2.micro KeyName='DevOpsKeyPair' SSHLocation=0.0.0.0/0"
        //sh "echo SKIPPING INFRASTRUCTURE CREATION/UPDATE for now .."
      }
    }
    stage('Update Inventory'){
      steps{
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
          //withCredentials([sshUserPrivateKey(credentialsId: 'cff1d3fe-236f-43ca-8ff5-5f37ec63422d', keyFileVariable: 'myKEY')]) {
          //  sh 'ansible-playbook ./ansible/playbooks/update_inventory.yml --user ubuntu --key-file ${myKEY}'  
          // }//end withCredentials
           withCredentials([sshUserPrivateKey(credentialsId: 'DevOpsKeyPair', keyFileVariable: 'myKEY')]) {
             sh 'ansible-playbook ./ansible/playbooks/update_inventory.yml --user ubuntu --key-file ${myKEY}'  
           }//end withCredentials

          sh "exit 0"
         }//end catchError

      }
    }
    stage('Configure Tomcat') {
      steps {
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
          //withCredentials([sshUserPrivateKey(credentialsId: 'cff1d3fe-236f-43ca-8ff5-5f37ec63422d', keyFileVariable: 'myKEY')]) {
          //   sh 'ansible-playbook ./ansible/playbooks/tomcat-setup.yml --user ubuntu --key-file ${myKEY}'  
          // }//end withCredentials
          withCredentials([sshUserPrivateKey(credentialsId: 'DevOpsKeyPair', keyFileVariable: 'myKEY')]) {
             sh 'ansible-playbook ./ansible/playbooks/tomcat-setup.yml --user ubuntu --key-file ${myKEY}'  
           }//end withCredentials
          sh "exit 0"
         }//end catchError
      }//end steps
    } //end stage
  } //end stages
}//end pipeline