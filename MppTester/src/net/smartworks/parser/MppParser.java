package net.smartworks.parser;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskContainer;
import net.sf.mpxj.mpp.MPPReader;

public class MppParser {

	public static void main(String[] args) throws MPXJException {
		
		MPPReader reader = new MPPReader();
		
		File file = new File(new java.io.File("").getAbsolutePath()+"/src/net/smartworks/mpp/testPrj.mpp");
		
		ProjectFile projectFile = reader.read(file);
		
		System.out.println("ALL TASK SIZE : " + projectFile.getAllTasks().size());
		
		TaskContainer tc = projectFile.getAllTasks();
		
		Iterator itr = tc.iterator();
		while (itr.hasNext()) {
			Task task = (Task)itr.next();
			
			
			System.out.println(task.getName());
			System.out.println("	id : " + task.getUniqueID());
			System.out.println("	outlineNo : " + task.getOutlineLevel());
			System.out.println("	MEMO : "+task.getNotes());
			System.out.println("	시작일 : "+task.getStart());
			System.out.println("	종료일 : "+task.getFinish());
			System.out.println("	소요일 : "+task.getDuration());
			

			if (task.getParentTask() != null) {
				Task parentTask = task.getParentTask();
				System.out.println("	부모 : "+parentTask.getName() + "(" +parentTask.getUniqueID()+ ")");
			}
			
			List<Relation> relations = task.getPredecessors();
			
			if (!relations.isEmpty()) {
				for (int i = 0; i < relations.size(); i++) {
					Relation relation = relations.get(i);
					Task preTask = relation.getTargetTask();
					System.out.println("	선행작업 "+i+" : " + preTask.getName());
				}
			}
			
			List<ResourceAssignment> resourceList = task.getResourceAssignments();
			if (resourceList.size() != 0) {
				for (int j = 0; j < resourceList.size(); j++) {
					ResourceAssignment ra = (ResourceAssignment)resourceList.get(j);
					Resource rs = ra.getResource();
					System.out.println("	담당자 "+j+" : " +rs.getName());
				}
			}
		}
	}
}
