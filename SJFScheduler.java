import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SJFScheduler {
    
    public static SchedulingResult schedule(List<Process> processes) {
        SchedulingResult result = new SchedulingResult();
        
        int currentTime = 0;
        int completed = 0;
        List<Process> remainingProcesses = new ArrayList<>(processes);
        
        while (completed < processes.size()) {
            // Find available processes (arrived and not completed)
            List<Process> availableProcesses = remainingProcesses.stream()
                    .filter(p -> p.getArrivalTime() <= currentTime && p.getCompletionTime() == 0)
                    .collect(Collectors.toList());
            
            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }
            
            // Find shortest job
            Process shortestProcess = availableProcesses.stream()
                    .min(Comparator.comparingInt(Process::getBurstTime))
                    .orElse(null);
            
            if (shortestProcess != null) {
                // Create Gantt entry
                result.addGanttEntry(new GanttEntry(shortestProcess.getName(), currentTime,
                        currentTime + shortestProcess.getBurstTime(), shortestProcess.getColor()));
                
                // Update process completion time
                currentTime += shortestProcess.getBurstTime();
                shortestProcess.setCompletionTime(currentTime);
                completed++;
            }
        }
        
        result.setProcesses(processes);
        return result;
    }
}
