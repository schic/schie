/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.model.LicenseInfo;

public class LicenseClient {

    private static Timer timer;

    public static void start() {
        stop();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                check();
            }
        };

        timer = new Timer(true);
        timer.scheduleAtFixedRate(task, 0, 24L * 60L * 60L * 1000L);
    }

    public static void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private static void check() {
        try {
            LicenseInfo licenseInfo = PlatformUI.MIRTH_FRAME.mirthClient.getLicenseInfo();

            if (licenseInfo.getExpirationDate() != null && licenseInfo.getExpirationDate() > 0) {
                final ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
                final ZonedDateTime expiration = ZonedDateTime.ofInstant(Instant.ofEpochMilli(licenseInfo.getExpirationDate()), ZoneId.systemDefault());

                Long warningPeriod = licenseInfo.getWarningPeriod();
                if (warningPeriod == null) {
                    warningPeriod = 7L * 24L * 60L * 60L * 1000L;
                }

                Long gracePeriod = licenseInfo.getGracePeriod();
                if (gracePeriod == null) {
                    gracePeriod = 7L * 24L * 60L * 60L * 1000L;
                }

                ZonedDateTime warningStart = expiration.minus(Duration.ofMillis(warningPeriod));
                ZonedDateTime graceEnd = expiration.plus(Duration.ofMillis(gracePeriod));

                if (now.isAfter(expiration) || now.isAfter(warningStart)) {
                    StringBuilder builder = new StringBuilder(Messages.getString("LicenseClient.0")).append(StringUtils.join(licenseInfo.getExtensions(), Messages.getString("LicenseClient.1"))).append(Messages.getString("LicenseClient.2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    Temporal endDate;

                    if (now.isAfter(expiration)) {
                        endDate = graceEnd;
                        builder.append(Messages.getString("LicenseClient.3")); //$NON-NLS-1$
                    } else {
                        endDate = expiration;
                        builder.append(Messages.getString("LicenseClient.4")); //$NON-NLS-1$
                    }

                    int days = (int) Math.ceil((double) Duration.between(now, endDate).getSeconds() / 60 / 60 / 24);
                    builder.append(days).append(Messages.getString("LicenseClient.5")).append(days == 1 ? Messages.getString("LicenseClient.6") : Messages.getString("LicenseClient.7")).append(Messages.getString("LicenseClient.8")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    final String message = builder.toString();

                    SwingUtilities.invokeLater(() -> {
                        if (now.isAfter(expiration)) {
                            PlatformUI.MIRTH_FRAME.alertError(PlatformUI.MIRTH_FRAME, message);
                        } else {
                            PlatformUI.MIRTH_FRAME.alertWarning(PlatformUI.MIRTH_FRAME, message);
                        }
                    });
                }
            }
        } catch (ClientException e) {
            // Ignore
        }
    }
}
