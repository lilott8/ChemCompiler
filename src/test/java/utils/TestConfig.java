package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import config.PhaseConfig;

/**
 * @created: 9/1/17
 * @since: 0.1
 * @project: ChemicalCompiler
 */
public class TestConfig implements PhaseConfig {

    List<String> files = new ArrayList<>();
    Set<String> phases = new HashSet<>();

    public TestConfig(String file) {
        files.add(file);
        phases.add("inference");
    }

    @Override
    public List<String> getFilesForCompilation() {
        return files;
    }

    @Override
    public Set<String> getAllPhases() {
        return phases;
    }

    @Override
    public boolean phaseEnabled(String phase) {
        return this.phases.contains(phase);
    }

    @Override
    public boolean phasesEnabled() {
        return !this.phases.isEmpty();
    }

    @Override
    public String getOutputDir() {
        return null;
    }

    @Override
    public boolean clean() {
        return false;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public int getNumberOfThreads() {
        return 0;
    }
}
