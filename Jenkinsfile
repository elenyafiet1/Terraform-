pipeline {
    agent any
    
    environment {
        // Windows-compatible workspace path
        WORKSPACE = "${env.WORKSPACE}".replace('/', '\\')
        
        // Change this to your actual Terraform directory name
        TF_DIR = "${WORKSPACE}\\terraform"
        
        // Timeout for pipeline (minutes)
        PIPELINE_TIMEOUT = '30'
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')  // Fixed: Using direct value instead of interpolation
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    stages {
        // ====================== DEBUGGING STAGE ======================
        stage('System Diagnostics') {
            steps {
                bat """
                echo [DEBUG] WORKSPACE: %WORKSPACE%
                echo [DEBUG] TF_DIR: %TF_DIR%
                echo [DEBUG] Listing workspace contents:
                dir "%WORKSPACE%"
                echo [DEBUG] System PATH:
                echo %PATH%
                echo [DEBUG] Terraform version:
                terraform --version || echo "Terraform not found"
                echo [DEBUG] Git version:
                git --version || echo "Git not found"
                """
            }
        }
        
        // ====================== CHECKOUT STAGE ======================
        stage('Checkout Code') {
            steps {
                checkout scm
                
                // Verify the Terraform directory exists
                bat """
                if not exist "%TF_DIR%" (
                    echo ERROR: Terraform directory not found at %TF_DIR%
                    echo Available directories:
                    dir /ad /b
                    exit 1
                )
                """
            }
        }
        
        // ====================== TERRAFORM INIT ======================
        stage('Terraform Init') {
            steps {
                script {
                    try {
                        bat """
                        cd /d "%TF_DIR%"
                        echo Initializing Terraform...
                        terraform init -input=false -no-color
                        """
                    } catch (Exception e) {
                        echo "Terraform init failed: ${e}"
                        bat """
                        cd /d "%TF_DIR%"
                        terraform init -input=false -no-color -upgrade
                        """
                    }
                }
            }
        }
        
        // ====================== TERRAFORM PLAN ======================
        stage('Terraform Plan') {
            steps {
                bat """
                cd /d "%TF_DIR%"
                echo Generating execution plan...
                terraform plan -out=tfplan -no-color
                """
            }
        }
        
        // ====================== APPROVAL GATE ======================
        stage('Manual Approval') {
            when {
                branch 'main'  // Only require approval for main branch
            }
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input message: 'Approve Terraform deployment?', 
                          ok: 'Deploy',
                          parameters: [
                              booleanParam(
                                  name: 'CONFIRM_DEPLOY',
                                  defaultValue: false,
                                  description: 'Check to approve deployment'
                              )
                          ]
                }
            }
        }
        
        // ====================== TERRAFORM APPLY ======================
        stage('Terraform Apply') {
            when {
                expression { 
                    return env.BRANCH_NAME == 'main' && 
                           params.CONFIRM_DEPLOY == true 
                }
            }
            steps {
                bat """
                cd /d "%TF_DIR%"
                echo Applying changes...
                terraform apply -auto-approve -no-color tfplan
                """
            }
        }
    }
    
    // ====================== POST-BUILD ACTIONS ======================
    post {
        always {
            bat 'echo Pipeline completed with status: %BUILD_STATUS%'
            archiveArtifacts artifacts: '**/tfplan', allowEmptyArchive: true
        }
        
        success {
            bat 'echo Pipeline succeeded!'
            // mail to: 'team@example.com', subject: 'Terraform Applied Successfully', body: '...'
        }
        
        failure {
            bat 'echo Pipeline failed!'
            // mail to: 'admin@example.com', subject: 'Terraform Pipeline Failed', body: '...'
        }
        
        cleanup {
            bat 'echo Cleaning up workspace...'
            // deleteDir()  // Uncomment to clean workspace after build
        }
    }
}
