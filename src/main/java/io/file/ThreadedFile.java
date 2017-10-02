package io.file;

import com.google.common.collect.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import config.Config;
import config.ConfigFactory;
import config.InferenceConfig;
import phases.inference.Inference;

/**
 * @created: 8/14/17
 * @since: 0.1
 * @project: chemtrails
 *
 * Handles a buffer queue to write to a disk.
 * This will shuffle a file every N writes.
 */
public class ThreadedFile extends FileHandler {

    private Queue<String> queue = new ConcurrentLinkedQueue<>();
    private boolean receivedDone = false;


    public static final Logger logger = LogManager.getLogger(ThreadedFile.class);

    public ThreadedFile() {
        super();
    }

    public ThreadedFile(String name) {
        super(name);
    }

    public void push(String item) {
        this.queue.add(item);
    }

    public void push(Set<String> items) {
        this.queue.addAll(items);
    }

    public void push(List<String> items) {
        this.queue.addAll(items);
    }

    public void sendDoneSignal() {
        logger.info("Caught the receivedDone signal!");
        this.receivedDone = true;
    }

    protected void openFile() {
        try {
            this.writer = new BufferedWriter(new FileWriter(this.getCurrentFile()));
        } catch(IOException e) {
            logger.error(String.format("Error closing: %s", this.getCurrentFile()));
            logger.error(e.toString());
        }
    }

    protected String getCurrentFile() {
        return String.format("%s%s_%d", this.outputDir, this.baseFile, this.fileNumber);
    }

    @Override
    public void run() {
        int currentWrite = 0;
        String item;
        // Open the file on run!
        this.openFile();

        // This catches the case where we are receivedDone, but the queue isn't empty.
        while (!this.receivedDone) {
            // Write to disk all the days of your life!
            while (!this.queue.isEmpty()) {
                item = this.queue.poll();
                if (!this.write(item)) {
                    this.invalid++;
                    // do something to requeue the data?
                }
                currentWrite++;
                // Change the file if we've reached max writes.
                if(currentWrite == MAX_WRITES) {
                    this.changeFile();
                    // Reset our counter to 0!
                    currentWrite = 0;
                }
            }
        }
        // Close the file once we are receivedDone.
        this.closeFile();
    }

    @Override
    public void writeTable(Table<Integer, Integer, Set<Integer>> table) {
        this.write(table);
    }
}
