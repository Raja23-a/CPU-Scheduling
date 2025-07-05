public class GanttEntry {
    private String processName;
    private int startTime;
    private int endTime;
    private String color;

    public GanttEntry(String processName, int startTime, int endTime, String color) {
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    // Getters and Setters
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }

    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getEndTime() { return endTime; }
    public void setEndTime(int endTime) { this.endTime = endTime; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getDuration() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return String.format("GanttEntry{process='%s', start=%d, end=%d, duration=%d}",
                processName, startTime, endTime, getDuration());
    }
}
