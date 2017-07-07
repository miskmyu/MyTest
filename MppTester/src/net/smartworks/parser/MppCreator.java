package net.smartworks.parser;

import java.io.IOException;
import java.util.Calendar;

import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mpx.MPXWriter;

public class MppCreator {
	
	static Task pre = null;
	
	public static void main(String[] args) {
 
		// writes the project-data-structure into a mpx-XML-File
		MPXWriter mpxwriter = new MPXWriter();
 
		// base data-structure for project files
		ProjectFile projectfile = new ProjectFile();
 
		// filling the project with some dummy data
		// here you can easily use your real data extracted
		// from a database or a csv-file
		int personcount = 1;
		for (int i = 1; i <= 10; i++) {
 
			// the same task as in ms project
			Task task = projectfile.addTask();
			task.setName("Example Task " + i);
 
			Task presub = null;
			// add some subtasks
			for (int j = 1; j <= 10; j++) {
 
				Task subtask = task.addTask();
				subtask.setName("Sub Task " + j);
 
				// set the subtasks duration, every subtask will take 4 hours in
				// the example
				subtask.setDuration(Duration.getInstance(4, TimeUnit.HOURS));
 
				// add resources to the subtask
				// in this example we will add only one resource to every task
				// 1. add the resource to the general projectfile
				Resource res = projectfile.addResource();
				res.setName("Person " + personcount);
				personcount++;
 
				// associate the resource with the current task
				subtask.addResourceAssignment(res);
 
				// concatenate the subtasks, so that one subtask is performed after
				// another on the timeline
				// the first task has no predecessor
				if (j == 1) {
					presub = subtask;
				} else {
					subtask.addPredecessor(presub, RelationType.FINISH_START, Duration
							.getInstance(0, TimeUnit.HOURS));
					presub = subtask;
				}
			}
 
			// concatenate the tasks, so that one main task is performed after
			// another on the timeline
			// the first task has no predecessor
			if (i == 1) {
				//set the startdate of the project
				Calendar rightNow = Calendar.getInstance();
				rightNow.set(2012, 1, 1);
				task.setStart(rightNow.getTime());
				pre = task;
			} else {
				task.addPredecessor(pre, RelationType.FINISH_START, Duration
						.getInstance(0, TimeUnit.HOURS));
				pre = task;
			}
		}
		//writing the project file
		try {
			mpxwriter.write(projectfile, "exampleproject.mpp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
