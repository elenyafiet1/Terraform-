pipeline {
    agent any
    
    environment {
        AWS_REGION = 'us-east-1'
    }
    
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
                    credentialsId: 'aws-terraform-creds',
                    accessKeyVariable: 'AKIAXKUH3BR5ZAS3J547',
                    secretKeyVariable: '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'
                ]]) {
                    dir('terraform') {
                        // Single bat command to preserve environment variables
                        bat '''
                            set AWS_ACCESS_KEY_ID=%AWS_ACCESS_KEY_ID%
                            set AWS_SECRET_ACCESS_KEY=%AWS_SECRET_ACCESS_KEY%
                            terraform plan
                        '''
                    }
                }
            }
        }
    }
}
