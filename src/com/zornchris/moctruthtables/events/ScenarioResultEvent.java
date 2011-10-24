package com.zornchris.moctruthtables.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.zornchris.moctruthtables.TruthTable;

public class ScenarioResultEvent extends Event implements Cancellable {
    protected static final long serialVersionUID = 1L;
    protected boolean cancelled;
    public boolean result;
    public TruthTable truthTable;
    
    public ScenarioResultEvent(boolean result, TruthTable truthTable) {
        super("ScenarioResultEvent");
        this.result = result;
        this.truthTable = truthTable;
    }

    @Override
    public void setCancelled(boolean bln) { cancelled = bln; }
    @Override
    public boolean isCancelled() { return cancelled; }
    
    public boolean getResult() { return result; }
}
