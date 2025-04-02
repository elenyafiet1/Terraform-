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
                bat 'terraform --version'
                bat 'cd terraform && terraform init'
            }
        }
        
        stage('Terraform Plan') {
            steps {
                bat 'cd terraform && terraform plan'
            }
        }
    }
}