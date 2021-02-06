// Declarative pipeline
pipeline {
  agent any
  stages {
    stage('Submit Stack') { 
      steps {
          catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
             //withCredentials([UsernamePasswordMultiBinding(credentialsId: 'Jenkins-server', keyFileVariable:'Jenkins-server')]) {
              //sh "aws cloudformation deploy --template-file  '$workspace/cloudformation/TrainingEvent-UbuntuServer.json' --stack-name TomCatWeb-Stack-Val --region 'us-east-1' --parameter-overrides InstanceType=t2.micro KeyName='myTestKeyPair02' SSHLocation=0.0.0.0/0"
              sh "aws s3 ls"
              //sh "echo SKIPPING INFRASTRUCTURE CREATION/UPDATE for now .."
            //}//end withCredentials
         }//end catcherror
      }
    }
    stage('Update Inventory'){
      steps{
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
           sh "echo SKIPPING Automated update of inventory file in lieu to manual update .."
           //withCredentials([sshUserPrivateKey(credentialsId: '2be93d5b-70f9-4db2-adc3-f307b7b86c48', keyFileVariable: 'myKEY')]) {
           //  sh 'ansible-playbook ./ansible/playbooks/update_inventory.yml --user ubuntu --key-file ${myKEY}'  
           //}//end withCredentials
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