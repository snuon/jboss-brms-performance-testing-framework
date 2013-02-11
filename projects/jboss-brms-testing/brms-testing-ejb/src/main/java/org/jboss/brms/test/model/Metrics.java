package org.jboss.brms.test.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.jboss.brms.test.service.MetricsService;

/**
 * Metrics for a single test run.
 */
@Entity
public class Metrics extends PersistentObject {
    /** Serial version identifier. */
    private static final long serialVersionUID = 1L;

    /** Number of machines used to run the processes. */
    @NotNull
    private Integer numberOfMachines;

    /** If multiple machines were used, was load balancing applied? */
    private Boolean loadBalancingUsed;

    /** Are the process instances started to run at the same time? */
    private Boolean processesStartedInParallel;

    /** Are the process instances each started in their own stateful knowledge session? */
    private Boolean processesRunInIndividualKnowledgeSession;

    /** The packages used in the test run. */
    @OneToMany(cascade = CascadeType.ALL)
    @Valid
    private Set<MeasuredPackage> packages;

    /** The moment the test run began. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date startingTime;

    /** The moment the test run ended. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingTime;

    /** Default constructor, required by JPA. */
    protected Metrics() {
    }

    /**
     * Parameterized constructor, for use by the {@link MetricsService}.
     * 
     * @param numberOfMachines
     *            Number of machines used to run the processes.
     * @param loadBalancingUsed
     *            If multiple machines were used, was load balancing applied?
     * @param processesStartedInParallel
     *            Are the process instances started to run at the same time?
     * @param processesRunInIndividualKnowledgeSession
     *            Are the process instances each started in their own stateful knowledge session?
     */
    public Metrics(final Integer numberOfMachines, final Boolean loadBalancingUsed, final Boolean processesStartedInParallel,
            final Boolean processesRunInIndividualKnowledgeSession) {
        this.numberOfMachines = numberOfMachines;
        this.loadBalancingUsed = loadBalancingUsed;
        this.processesStartedInParallel = processesStartedInParallel;
        this.processesRunInIndividualKnowledgeSession = processesRunInIndividualKnowledgeSession;
    }

    public Integer getNumberOfMachines() {
        return numberOfMachines;
    }

    void setNumberOfMachines(final Integer numberOfMachines) {
        this.numberOfMachines = numberOfMachines;
    }

    public Boolean getLoadBalancingUsed() {
        return loadBalancingUsed;
    }

    void setLoadBalancingUsed(final Boolean loadBalancingUsed) {
        this.loadBalancingUsed = loadBalancingUsed;
    }

    public Boolean getProcessesStartedInParallel() {
        return processesStartedInParallel;
    }

    void setProcessesStartedInParallel(final Boolean processesStartedInParallel) {
        this.processesStartedInParallel = processesStartedInParallel;
    }

    public Boolean getProcessesRunInIndividualKnowledgeSession() {
        return processesRunInIndividualKnowledgeSession;
    }

    void setProcessesRunInIndividualKnowledgeSession(final Boolean processesRunInIndividualKnowledgeSession) {
        this.processesRunInIndividualKnowledgeSession = processesRunInIndividualKnowledgeSession;
    }

    public Set<MeasuredPackage> getPackages() {
        if (packages == null) {
            packages = new HashSet<MeasuredPackage>();
        }
        return packages;
    }

    void setPackages(final Set<MeasuredPackage> packages) {
        this.packages = packages;
    }

    public boolean addPackage(final MeasuredPackage pakkage) {
        return getPackages().add(pakkage);
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(final Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(final Date endingTime) {
        this.endingTime = endingTime;
    }

    public Long getTimeToComplete() {
        Long timeToComplete = null;
        if ((startingTime != null) && (endingTime != null)) {
            timeToComplete = endingTime.getTime() - startingTime.getTime();
        }
        return timeToComplete;
    }

    public String print() {
        final StringBuilder sb = new StringBuilder().append("\nMetrics:\n * Number of machines: ").append(numberOfMachines)
                .append("\n * Was load balancing used: ").append(loadBalancingUsed).append("\n * Were processes started in parallel: ")
                .append(processesStartedInParallel).append("\n * Were processes run in an individual knowledge session: ")
                .append(processesRunInIndividualKnowledgeSession);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        if (endingTime != null) {
            sb.append("\n\n * Duration: ").append(endingTime.getTime() - startingTime.getTime()).append(" ms (starting time = ")
                    .append(timeFormat.format(startingTime)).append(", ending time = ").append(timeFormat.format(endingTime)).append(")");
        } else if (startingTime != null) {
            sb.append("\n\n * Test started at ").append(timeFormat.format(startingTime)).append(" but did not end yet.");
        } else {
            sb.append("\n\n * Test not started yet.");
        }
        return sb.toString();
    }

    public String printAll() {
        final StringBuilder sb = new StringBuilder(print());
        for (final MeasuredPackage mpak : getPackages()) {
            sb.append(mpak.print());
            for (final MeasuredProcess mp : mpak.getProcesses()) {
                sb.append(mp.print());
                for (final MeasuredProcessInstance mpi : mp.getInstances()) {
                    sb.append(mpi.print());
                    for (final MeasuredRule mr : mpi.getRules()) {
                        sb.append(mr.print());
                    }
                    for (final MeasuredHumanTask mht : mpi.getHumanTasks()) {
                        sb.append(mht.print());
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        // Shortcuts.
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Metrics)) {
            return false;
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Metrics [numberOfMachines=").append(numberOfMachines).append(", loadBalancingUsed=").append(loadBalancingUsed)
                .append(", processesStartedInParallel=").append(processesStartedInParallel).append(", processesRunInIndividualKnowledgeSession=")
                .append(processesRunInIndividualKnowledgeSession).append(", startingTime=").append(startingTime).append(", endingTime=").append(endingTime)
                .append("]").toString();
    }
}
