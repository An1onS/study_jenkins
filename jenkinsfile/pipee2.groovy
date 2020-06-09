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
                    sh "cp /var/lib/jenkins/workspace/pipee2/pipee2/Dockerfile /home/adminci/study_ansible/SakharovAY/Dockerfile"
                    
                    sh "docker build /home/adminci/study_ansible/SakharovAY/ -t pipee2"
                    sh "docker run -itd --name pipee2container pipee2"
                    sh "docker exec pipee2container "df -h > dfh.txt""
                    sh "docker exec pipee2container "grep cpu /proc/stat > cpu.txt""
                }
            }
        }        
    }    
}
