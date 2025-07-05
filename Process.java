public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int remainingTime;
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private String color;

    public Process(String name, int arrivalTime, int burstTime, int priority, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.color = color;
    }

    // Copy constructor
    public Process(Process other) {
        this.name = other.name;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.priority = other.priority;
        this.remainingTime = other.burstTime;
        this.completionTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.color = other.color;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getBurstTime() { return burstTime; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }

    public int getCompletionTime() { return completionTime; }
    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }

    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }

    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public void resetProcess() {
        this.remainingTime = this.burstTime;
        this.completionTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
    }

    @Override
    public String toString() {
        return String.format("Process{name='%s', arrival=%d, burst=%d, priority=%d, completion=%d, turnaround=%d, waiting=%d}",
                name, arrivalTime, burstTime, priority, completionTime, turnaroundTime, waitingTime);
    }
}
