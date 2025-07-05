import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SRTFScheduler {
    
    public static SchedulingResult schedule(List<Process> processes) {
        SchedulingResult result = new SchedulingResult();
        
        int currentTime = 0;
        int completed = 0;
        List<Process> allProcesses = new ArrayList<>(processes);
        
        while (completed < processes.size()) {
            // Find available processes (arrived and not completed)
            List<Process> availableProcesses = allProcesses.stream()
                    .filter(p -> p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0)
                    .collect(Collectors.toList());
            
            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }
            
            // Find process with shortest remaining time
            Process shortestRemainingProcess = availableProcesses.stream()
                    .min(Comparator.comparingInt(Process::getRemainingTime))
                    .orElse(null);
            
            if (shortestRemainingProcess != null) {
                // Execute for 1 time unit
                result.addGanttEntry(new GanttEntry(shortestRemainingProcess.getName(), currentTime,
                        currentTime + 1, shortestRemainingProcess.getColor()));
                
                currentTime += 1;
                shortestRemainingProcess.setRemainingTime(shortestRemainingProcess.getRemainingTime() - 1);
                
                // Check if process is completed
                if (shortestRemainingProcess.getRemainingTime() == 0) {
                    shortestRemainingProcess.setCompletionTime(currentTime);
                    completed++;
                }
            }
        }
        
        result.setProcesses(processes);
        return result;
    }
}
