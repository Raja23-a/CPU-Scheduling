import java.util.*;
import java.util.stream.Collectors;

public class CPUScheduler {
    
    public enum Algorithm {
        FCFS, SJF, RR, PRIORITY, SRTF
    }

    public static SchedulingResult schedule(List<Process> processes, Algorithm algorithm, int timeQuantum) {
        SchedulingResult result = new SchedulingResult();
        
        // Deep copy processes to avoid modifying original
        List<Process> processesCopy = processes.stream()
                .map(Process::new)
                .collect(Collectors.toList());

        // Reset all processes
        processesCopy.forEach(Process::resetProcess);

        switch (algorithm) {
            case FCFS:
                result = FCFSScheduler.schedule(processesCopy);
                break;
            case SJF:
                result = SJFScheduler.schedule(processesCopy);
                break;
            case RR:
                result = RoundRobinScheduler.schedule(processesCopy, timeQuantum);
                break;
            case PRIORITY:
                result = PriorityScheduler.schedule(processesCopy);
                break;
            case SRTF:
                result = SRTFScheduler.schedule(processesCopy);
                break;
        }

        result.calculateMetrics();
        return result;
    }

    // Utility method to merge consecutive Gantt entries for the same process
    public static List<GanttEntry> mergeConsecutiveEntries(List<GanttEntry> ganttChart) {
        if (ganttChart.isEmpty()) return ganttChart;
        
        List<GanttEntry> merged = new ArrayList<>();
        GanttEntry current = ganttChart.get(0);
        
        for (int i = 1; i < ganttChart.size(); i++) {
            GanttEntry next = ganttChart.get(i);
            
            if (current.getProcessName().equals(next.getProcessName()) && 
                current.getEndTime() == next.getStartTime()) {
                // Merge entries
                current = new GanttEntry(current.getProcessName(), current.getStartTime(),
                        next.getEndTime(), current.getColor());
            } else {
                // Add current entry and move to next
                merged.add(current);
                current = next;
            }
        }
        
        merged.add(current);
        return merged;
    }

    // Method to convert result to JSON string (for web integration)
    public static String toJSON(SchedulingResult result) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Gantt Chart
        json.append("  \"ganttChart\": [\n");
        List<GanttEntry> mergedChart = mergeConsecutiveEntries(result.getGanttChart());
        for (int i = 0; i < mergedChart.size(); i++) {
            GanttEntry entry = mergedChart.get(i);
            json.append("    {\n");
            json.append("      \"processName\": \"").append(entry.getProcessName()).append("\",\n");
            json.append("      \"startTime\": ").append(entry.getStartTime()).append(",\n");
            json.append("      \"endTime\": ").append(entry.getEndTime()).append(",\n");
            json.append("      \"color\": \"").append(entry.getColor()).append("\"\n");
            json.append("    }").append(i < mergedChart.size() - 1 ? "," : "").append("\n");
        }
        json.append("  ],\n");
        
        // Processes
        json.append("  \"processes\": [\n");
        List<Process> processes = result.getProcesses();
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            json.append("    {\n");
            json.append("      \"name\": \"").append(process.getName()).append("\",\n");
            json.append("      \"arrivalTime\": ").append(process.getArrivalTime()).append(",\n");
            json.append("      \"burstTime\": ").append(process.getBurstTime()).append(",\n");
            json.append("      \"priority\": ").append(process.getPriority()).append(",\n");
            json.append("      \"completionTime\": ").append(process.getCompletionTime()).append(",\n");
            json.append("      \"turnaroundTime\": ").append(process.getTurnaroundTime()).append(",\n");
            json.append("      \"waitingTime\": ").append(process.getWaitingTime()).append(",\n");
            json.append("      \"color\": \"").append(process.getColor()).append("\"\n");
            json.append("    }").append(i < processes.size() - 1 ? "," : "").append("\n");
        }
        json.append("  ],\n");
        
        // Metrics
        json.append("  \"metrics\": {\n");
        json.append("    \"avgTurnaroundTime\": ").append(String.format("%.2f", result.getAvgTurnaroundTime())).append(",\n");
        json.append("    \"avgWaitingTime\": ").append(String.format("%.2f", result.getAvgWaitingTime())).append(",\n");
        json.append("    \"totalExecutionTime\": ").append(result.getTotalExecutionTime()).append(",\n");
        json.append("    \"throughput\": ").append(String.format("%.2f", result.getThroughput())).append("\n");
        json.append("  }\n");
        
        json.append("}");
        return json.toString();
    }

    // Method to display results in a formatted way
    public static void displayResults(SchedulingResult result, String algorithmName) {
        System.out.println("\n=== " + algorithmName + " SCHEDULING RESULTS ===");
        
        // Display Gantt Chart
        System.out.println("\nGantt Chart:");
        List<GanttEntry> mergedChart = mergeConsecutiveEntries(result.getGanttChart());
        for (GanttEntry entry : mergedChart) {
            System.out.printf("| %s(%d-%d) ", entry.getProcessName(), entry.getStartTime(), entry.getEndTime());
        }
        System.out.println("|");
        
        // Display Process Table
        System.out.println("\nProcess Details:");
        System.out.println("Process\tArrival\tBurst\tPriority\tCompletion\tTurnaround\tWaiting");
        for (Process process : result.getProcesses()) {
            System.out.printf("%s\t%d\t%d\t%d\t\t%d\t\t%d\t\t%d\n",
                    process.getName(), process.getArrivalTime(), process.getBurstTime(),
                    process.getPriority(), process.getCompletionTime(),
                    process.getTurnaroundTime(), process.getWaitingTime());
        }
        
        // Display Metrics
        System.out.println("\nPerformance Metrics:");
        System.out.printf("Average Turnaround Time: %.2f\n", result.getAvgTurnaroundTime());
        System.out.printf("Average Waiting Time: %.2f\n", result.getAvgWaitingTime());
        System.out.printf("Total Execution Time: %d\n", result.getTotalExecutionTime());
        System.out.printf("Throughput: %.2f processes/unit time\n", result.getThroughput());
    }

    // Main method for testing
    public static void main(String[] args) {
        // Test the scheduler with sample processes
        List<Process> processes = Arrays.asList(
            new Process("P1", 0, 5, 2, "#3498db"),
            new Process("P2", 1, 3, 1, "#e74c3c"),
            new Process("P3", 2, 8, 3, "#2ecc71"),
            new Process("P4", 3, 6, 2, "#f39c12")
        );

        System.out.println("CPU Scheduling Simulator");
        System.out.println("========================");
        System.out.println("Sample Processes:");
        System.out.println("P1: Arrival=0, Burst=5, Priority=2");
        System.out.println("P2: Arrival=1, Burst=3, Priority=1");
        System.out.println("P3: Arrival=2, Burst=8, Priority=3");
        System.out.println("P4: Arrival=3, Burst=6, Priority=2");

        // Test all algorithms
        for (Algorithm algorithm : Algorithm.values()) {
            int timeQuantum = (algorithm == Algorithm.RR) ? 2 : 0;
            SchedulingResult result = schedule(processes, algorithm, timeQuantum);
            displayResults(result, algorithm.toString());
        }
    }
}
