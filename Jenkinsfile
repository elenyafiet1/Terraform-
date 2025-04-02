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
                    accessKeyVariable: 'AKIAXKUH3BR5UBMBUDMV',
                    secretKeyVariable: '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'
                ]]) {
                    dir('terraform') {
                        bat """
                            set AWS_ACCESS_KEY_ID=%AWS_ACCESS_KEY_ID%
                            set AWS_SECRET_ACCESS_KEY=%AWS_SECRET_ACCESS_KEY%
                            terraform plan
                        """
                    }
                }
            }
        }
    }
}
