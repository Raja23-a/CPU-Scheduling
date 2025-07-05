import java.util.Comparator;
import java.util.List;

public class FCFSScheduler {
    
    public static SchedulingResult schedule(List<Process> processes) {
        SchedulingResult result = new SchedulingResult();
        
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        
        int currentTime = 0;
        
        for (Process process : processes) {
            // If current time is less than arrival time, advance to arrival time
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            
            // Create Gantt entry
            result.addGanttEntry(new GanttEntry(process.getName(), currentTime, 
                    currentTime + process.getBurstTime(), process.getColor()));
            
            // Update process completion time
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);
        }
        
        result.setProcesses(processes);
        return result;
    }
}
