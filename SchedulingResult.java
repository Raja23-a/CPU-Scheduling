import java.util.ArrayList;
import java.util.List;

public class SchedulingResult {
    private List<GanttEntry> ganttChart;
    private List<Process> processes;
    private double avgTurnaroundTime;
    private double avgWaitingTime;
    private int totalExecutionTime;
    private double throughput;

    public SchedulingResult() {
        this.ganttChart = new ArrayList<>();
        this.processes = new ArrayList<>();
    }

    // Getters and Setters
    public List<GanttEntry> getGanttChart() { return ganttChart; }
    public void setGanttChart(List<GanttEntry> ganttChart) { this.ganttChart = ganttChart; }

    public List<Process> getProcesses() { return processes; }
    public void setProcesses(List<Process> processes) { this.processes = processes; }

    public double getAvgTurnaroundTime() { return avgTurnaroundTime; }
    public void setAvgTurnaroundTime(double avgTurnaroundTime) { this.avgTurnaroundTime = avgTurnaroundTime; }

    public double getAvgWaitingTime() { return avgWaitingTime; }
    public void setAvgWaitingTime(double avgWaitingTime) { this.avgWaitingTime = avgWaitingTime; }

    public int getTotalExecutionTime() { return totalExecutionTime; }
    public void setTotalExecutionTime(int totalExecutionTime) { this.totalExecutionTime = totalExecutionTime; }

    public double getThroughput() { return throughput; }
    public void setThroughput(double throughput) { this.throughput = throughput; }

    public void addGanttEntry(GanttEntry entry) {
        this.ganttChart.add(entry);
    }

    public void calculateMetrics() {
        if (processes.isEmpty()) return;

        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        for (Process process : processes) {
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            totalTurnaroundTime += process.getTurnaroundTime();
            totalWaitingTime += process.getWaitingTime();
        }

        this.avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        this.avgWaitingTime = (double) totalWaitingTime / processes.size();
        this.totalExecutionTime = processes.stream().mapToInt(Process::getCompletionTime).max().orElse(0);
        this.throughput = processes.size() / (double) totalExecutionTime;
    }
}
