/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.exception.ValidationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskExpirationFilter {

    private static final Pattern PATTERN = Pattern.compile("^(-?[0-9]+)([smhd]?)$");
    
    private enum Unit {
        s(ChronoUnit.SECONDS,   ChronoUnit.MILLIS), 
        m(ChronoUnit.MINUTES,   ChronoUnit.SECONDS), 
        h(ChronoUnit.HOURS,     ChronoUnit.MINUTES), 
        d(ChronoUnit.DAYS,      ChronoUnit.HOURS);
        
        private ChronoUnit chronoUnit;
        private ChronoUnit adjustUnit;

        private Unit(ChronoUnit chronoUnit, ChronoUnit adjustUnit) {
            this.chronoUnit = chronoUnit;
            this.adjustUnit = adjustUnit;
        }
        
        public ChronoUnit chronoUnit() {
            return chronoUnit;
        }

        public ChronoUnit adjustUnit() {
            return adjustUnit;
        }
    }
    
    private Date value;

    public TaskExpirationFilter(Date value) {
        this.value = value;
    }

    public Date getValue() {
        return value;
    }
    
    public static TaskExpirationFilter of(String expiration) throws ValidationException {
        
        Matcher matcher;
        Long amount;
        Unit unit;
        
        if (StringUtils.isBlank(expiration)) {
            return new TaskExpirationFilter(null);
        }
        matcher = PATTERN.matcher(expiration);
        if (matcher.find()) {
            amount = Long.parseLong(matcher.group(1));
            unit = matcher.groupCount() == 2 ? 
                Unit.valueOf(matcher.group(2)) : 
                Unit.d;
            return new TaskExpirationFilter(
                Date.from(Instant
                    .now()
                    .plus(1 + amount, unit.chronoUnit())
                    .truncatedTo(unit.adjustUnit())));
        } else {
            throw new ValidationException(String.format(
                "expiration: invalid value, allowed format is '%s' and default unit is days (d)",
                PATTERN.pattern()));
        }
    }        
}
