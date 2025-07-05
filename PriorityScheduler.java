import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityScheduler {
    
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
            
            // Find highest priority process (lower number = higher priority)
            Process highestPriorityProcess = availableProcesses.stream()
                    .min(Comparator.comparingInt(Process::getPriority))
                    .orElse(null);
            
            if (highestPriorityProcess != null) {
                // Create Gantt entry
                result.addGanttEntry(new GanttEntry(highestPriorityProcess.getName(), currentTime,
                        currentTime + highestPriorityProcess.getBurstTime(), highestPriorityProcess.getColor()));
                
                // Update process completion time
                currentTime += highestPriorityProcess.getBurstTime();
                highestPriorityProcess.setCompletionTime(currentTime);
                completed++;
            }
        }
        
        result.setProcesses(processes);
        return result;
    }
}
