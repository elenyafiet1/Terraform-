pipeline {
    agent any
    
    environment {
        AWS_REGION = 'us-east-1'
        TF_VERSION = '1.5.0' 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm  // Automatically checks out your repo
            }
        }
        
        stage('Terraform Init') {
            steps {
                sh '''
                cd infrastructure/
                terraform init -backend-config="bucket=my-tfstate-bucket"
                '''
            }
        }
    }
}