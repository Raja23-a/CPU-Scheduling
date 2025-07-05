import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinScheduler {
    
    public static SchedulingResult schedule(List<Process> processes, int timeQuantum) {
        SchedulingResult result = new SchedulingResult();
        
        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> allProcesses = new ArrayList<>(processes);
        
        // Sort by arrival time
        allProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));
        
        int currentTime = 0;
        int processIndex = 0;
        int completed = 0;
        
        // Add first process to ready queue
        if (!allProcesses.isEmpty() && allProcesses.get(0).getArrivalTime() <= currentTime) {
            readyQueue.offer(allProcesses.get(processIndex++));
        }
        
        while (completed < processes.size()) {
            if (readyQueue.isEmpty()) {
                // No process in ready queue, advance time
                if (processIndex < allProcesses.size()) {
                    currentTime = allProcesses.get(processIndex).getArrivalTime();
                    readyQueue.offer(allProcesses.get(processIndex++));
                }
                continue;
            }
            
            Process currentProcess = readyQueue.poll();
            int executeTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            
            // Create Gantt entry
            result.addGanttEntry(new GanttEntry(currentProcess.getName(), currentTime,
                    currentTime + executeTime, currentProcess.getColor()));
            
            currentTime += executeTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executeTime);
            
            // Add newly arrived processes to ready queue
            while (processIndex < allProcesses.size() && 
                   allProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.offer(allProcesses.get(processIndex++));
            }
            
            // Check if current process is completed
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                completed++;
            } else {
                // Add back to ready queue if not completed
                readyQueue.offer(currentProcess);
            }
        }
        
        result.setProcesses(processes);
        return result;
    }
}
