// This array contains folders which will group together the different jobs.
def folders = [
    [name: 'PetClinic', description: 'A folder to gorup PetClinic pipelines.']
]

// This array contains information about the pipelines we wish to be created.
// Note we can group these in a folder, for example "name: 'folder1/job1'".
// We are also providing a path to the Jenkinsfiles.
def pipelines = [
    [name: 'HelloWorld',           scriptPath: 'Hello_World/Jenkinsfile'               ],
    [name: 'HelloWorldDocker',     scriptPath: 'Hello_World_Docker/Jenkinsfile'        ],
    [name: 'ConsoleAppDNC',        scriptPath: 'Console_App_DNC/Jenkinsfile'           ],
    [name: 'PetClinic/PetClinicWebsiteJava', scriptPath: 'Pet_Clinic_Website_Java/Jenkinsfile'        ],
    [name: 'PetClinic/PetClinicAgentImage', scriptPath: 'Pet_Clinic_Website_Java/Jenkinsfile.build'   ]
]

// Looping over the folder array and creating folders specified.
for (f in folders) 
{
	folder("${f.name}")
	{
		displayName("${f.name}")
		description("${f.description}")
	}
}

// Looping over the pipeline objects.
for(p in pipelines) 
{
	
	// creating a job from the name property of the pipeline object.
	pipelineJob("${p.name}") 
	{
	    definition 
		{
	        cpsScm 
			{
	            scm 
				{
	                // Specifying the github repo we want to find our Jenkinsfiles.
					git 
					{
	                  remote 
					  {
	                    name('github')
	                    url('https://github.com/whayward-stfc/jenkins-test-applications.git')
	                  }
	                  branch('master')
	                  // Specifying additional options that we would usually set in the UI.
					  extensions 
					  {
	                  	cloneOptions 
						{
	                  	  shallow(true)
	                  	  depth(1)
						  noTags(true)
	                  	}
	                  }
	                }
	            }
				// Specifying the Jenkinsfiles that we passed into the object at the top.
	            scriptPath("${p.scriptPath}")
	        }
	    }
	    // This trigger runs the job once per day.
		triggers 
		{
	        cron('H H(1-8) * * *')
	    }
	}
}