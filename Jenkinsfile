pipeline {
    agent any
    
    environment {
        // Use Jenkins-provided workspace path
        TF_DIR = "${env.WORKSPACE}\\terraform"
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    stages {
        // ====================== VERIFICATION STAGE ======================
        stage('Verify Workspace') {
            steps {
                script {
                    // List all files for debugging
                    bat """
                    echo WORKSPACE PATH: %WORKSPACE%
                    echo CONTENTS:
                    dir /s /b
                    """
                    
                    // Check if terraform directory exists
                    def tfExists = bat(
                        script: 'if exist "terraform" (exit 0) else (exit 1)',
                        returnStatus: true
                    ) == 0
                    
                    if (!tfExists) {
                        error("Terraform directory not found in workspace")
                    }
                }
            }
        }
        
        // ====================== TERRAFORM STAGES ======================
        stage('Terraform Init') {
            steps {
                bat """
                cd /d "%TF_DIR%"
                echo Initializing Terraform...
                terraform init -input=false -no-color
                """
            }
        }
        
        stage('Terraform Plan') {
            steps {
                bat """
                cd /d "%TF_DIR%"
                terraform plan -out=tfplan -no-color
                """
            }
        }
        
        stage('Terraform Apply') {
            steps {
                bat """
                cd /d "%TF_DIR%"
                terraform apply -auto-approve tfplan
                """
            }
        }
    }
    
    post {
        always {
            bat 'echo Pipeline status: %BUILD_STATUS%'
        }
    }
}
