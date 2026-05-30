pipeline {
    agent any
    stages {
        stage('Git Pull') {
            steps {
                  git branch: 'main', url: 'https://github.com/swatiV-27/flight-reservation-app.git'
            }
        }
        stage('CODE BUILD') {
            steps {
                sh '''
                   cd FlightReservationApplication
                   mvn clean package
                   '''
            }
        }
        stage('QA-TEST') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-token') {
                    sh '''
                    cd FlightReservationApplication
                    mvn sonar:sonar -Dsonar.projectKey=flight-reservation-backend 
                 }    
            }
        }
        stage('Docker') {
            steps {
                sh '''
                    cd FlightReservationApplication
                    docker build -t swativ27/flight-reservation-pls-19-20:latest . 
                    docker push swativ27/flight-reservation-pls-19-20:latest 
                    docker rmi swativ27/flight-reservation-pls-19-20:latest
                    '''
            }
        }
       stage('Deploy') {
            steps {
                sh '''
                     cd FlightReservationApplication
                     kubectl apply -f k8s/deployment.yaml
                     kubectl apply -f k8s/service.yaml
                  '''
            }
        }
    }
}
