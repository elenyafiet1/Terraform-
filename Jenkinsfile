pipeline {
  agent any
  
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    
    stage('Terraform Init') {
      steps {
        dir('terraform') {
          bat 'terraform init'
        }
      }
    }
    
    stage('Terraform Plan') {
      steps {
        withCredentials([[
          $class: 'AmazonWebServicesCredentialsBinding',
          accessKeyVariable: 'AKIAXKUH3BR5UBMBUDMV',
          secretKeyVariable: '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',
          credentialsId: 'aws-terraform-creds' // Update this
        ]]) {
          dir('terraform') {
            bat 'terraform plan'
          }
        }
      }
    }
  }
}
