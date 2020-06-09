pipeline {
    agent{node('master')}
    stages {
        stage('Dowload project from github') {
            steps {
                script {
                    cleanWs()                    
                }
                script {
                    echo 'Downloading...'
                    checkout([$class                           : 'GitSCM',
                              branches                         : [[name: '*/master']],
                              doGenerateSubmoduleConfigurations: false,
                              extensions                       : [[$class           : 'RelativeTargetDirectory',
                                                                   relativeTargetDir: 'pipee2']],
                              submoduleCfg                     : [],
                              userRemoteConfigs                : [[credentialsId: 'IlyaBurakGit', url: 'https://github.com/An1onS/study_jenkins.git']]])
                }
            }
        }
        stage ('Run'){
            steps{
                script{
                    sh "docker build ${WORKSPACE}/pipee2 -t pipee2"
                    sh "docker run -d pipee2"
                    sh "docker exec -it pipee2 "df -h > dfh.txt""
                    sh 'docker exec pipee2container grep cpu /proc/stat > cpu.txt'
                }
            }
        }        
    }    
}
