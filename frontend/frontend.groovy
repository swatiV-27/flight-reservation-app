pipeline{
    agent any 
    stages{
        stage('code-pull'){
            steps{    
                 git branch: 'main', url: 'https://github.com/mayurmwagh/flight-reservation-app.git

            }
        }
        stage('CODE-build'){
            steps{
                sh '''
                    cd frontend
                    npm install
                    npm run build

                '''
            }
        }
        stage('deploy'){
            steps{
                sh '''
                    cd frontend
                    aws s3 sync dist/ s3://cblkc-front12end2104-project-bux/
                '''
            }
        }
    }
}
