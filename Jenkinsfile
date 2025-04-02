// Jenkinsfile for Windows Terraform Pipeline with SCM
pipeline {
    agent {
        label 'windows'  // Targets Windows agents specifically
    }
    
    tools {
        terraform 'Terraform-Windows'  // Must match Global Tools config
    }
    
    environment {
        // Customize these for your environment
        TF_ROOT_DIR = "${WORKSPACE}\\terraform"
        TF_STATE_BUCKET = "your-terraform-state-bucket"
        AWS_REGION = "us-east-1"
        
        // For Azure (example):
        // ARM_SUBSCRIPTION_ID = credentials('azure-subscription-id')
    }
    
    parameters {
        choice(
            name: 'TF_ACTION',
            choices: ['plan', 'apply', 'destroy'],
            description: 'Select Terraform action'
        )
        string(
            name: 'TF_VARS_FILE',
            defaultValue: 'terraform.tfvars',
            description: 'Path to variables file'
        )
    }
    
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm  // Checks out the configured repository
                bat "dir /s /b"  // Lists all files (debugging)
            }
        }
        
        stage('Terraform Init') {
            steps {
                dir(env.TF_ROOT_DIR) {
                    withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        credentialsId: 'aws-creds',
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                    ]]) {
                        bat """
                            terraform init \
                                -backend-config="bucket=${TF_STATE_BUCKET}" \
                                -backend-config="key=terraform.tfstate" \
                                -backend-config="region=${AWS_REGION}" \
                                -reconfigure
                        """
                    }
                }
            }
        }
        
        stage('Terraform Validate') {
            steps {
                dir(env.TF_ROOT_DIR) {
                    bat 'terraform validate'
                }
            }
        }
        
        stage('Terraform Plan/Apply') {
            steps {
                dir(env.TF_ROOT_DIR) {
                    script {
                        if (params.TF_ACTION == 'plan') {
                            bat """
                                terraform plan \
                                    -var-file="${params.TF_VARS_FILE}" \
                                    -out=tfplan
                            """
                            archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: false
                        } else if (params.TF_ACTION == 'apply') {
                            // For safety, you might want manual approval here
                            input message: 'Confirm Terraform Apply?', ok: 'Apply'
                            bat 'terraform apply -input=false tfplan'
                        } else if (params.TF_ACTION == 'destroy') {
                            input message: 'CONFIRM DESTROY? THIS IS DESTRUCTIVE!', ok: 'Destroy'
                            bat """
                                terraform plan -destroy \
                                    -var-file="${params.TF_VARS_FILE}" \
                                    -out=tfdestroyplan
                            """
                            bat 'terraform apply -input=false tfdestroyplan'
                        }
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Clean up sensitive files
            dir(env.TF_ROOT_DIR) {
                bat 'del /f *.tfvars *.tfplan *.tfstate*'  // Adjust as needed
            }
            
            // Send notification
            emailext (
                subject: "Terraform Pipeline ${currentBuild.currentResult}: ${env.JOB_NAME}",
                body: """
                    Pipeline ${currentBuild.currentResult}\n
                    Job: ${env.JOB_NAME}\n
                    Build: ${env.BUILD_NUMBER}\n
                    Action: ${params.TF_ACTION}\n
                    URL: ${env.BUILD_URL}
                """,
                to: 'your-team@example.com'
            )
        }
        
        success {
            echo 'Terraform execution completed successfully'
        }
        
        failure {
            echo 'Terraform execution failed'
            // Additional failure handling
        }
    }
}