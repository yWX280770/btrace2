/*
 * Copyright (c) 2007, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.opensolaris.os.dtrace;

import java.util.EventListener;

/**
 * Listener for data generated by a single DTrace {@link Consumer}.
 *
 * @author Tom Erickson
 */
public interface ConsumerListener extends EventListener {
    /**
     * Called whenever a DTrace probe fires (that is, once for each
     * instance of {@link ProbeData} generated by DTrace).  Identifies
     * the probe and provides data generated by the probe's actions.  To
     * terminate the consumer in the event of unexpected data, throw a
     * {@link ConsumerException} from this method.
     *
     * @throws ConsumerException if the implementation should terminate
     * the running consumer
     */
    public void dataReceived(DataEvent e) throws ConsumerException;

    /**
     * Called when traced data is dropped because of inadequate buffer
     * space.  To terminate the consumer in the event of a drop, throw
     * a {@link ConsumerException} from this method.
     *
     * @throws ConsumerException if the implementation should terminate
     * the running consumer
     */
    public void dataDropped(DropEvent e) throws ConsumerException;

    /**
     * Called when an error is encountered in the native DTrace library
     * while tracing probe data.  To terminate the consumer, throw a
     * {@link ConsumerException} from this method.
     *
     * @throws ConsumerException if the implementation should terminate
     * the running consumer
     */
    public void errorEncountered(ErrorEvent e) throws ConsumerException;

    /**
     * Called when the state of a target process changes.  To terminate
     * the consumer in the event of unexpected process state, throw a
     * {@link ConsumerException} from this method.
     *
     * @throws ConsumerException if the implementation should terminate
     * the running consumer
     * @see Consumer#createProcess(String command)
     * @see Consumer#grabProcess(int pid)
     */
    public void processStateChanged(ProcessEvent e) throws ConsumerException;

    /**
     * Called once when the source {@link Consumer} is successfully
     * started in response to {@link Consumer#go()}.
     *
     * @see #consumerStopped(ConsumerEvent e)
     */
    public void consumerStarted(ConsumerEvent e);

    /**
     * Called once when the source {@link Consumer} is stopped,
     * indicating that this listener should expect no further events.
     * Guaranteed to be called whether the consumer was stopped by
     * request (by calling {@link Consumer#stop()} or {@link
     * Consumer#abort()}), terminated normally as a result of the DTrace
     * {@code exit()} action (see <a
     * href=http://docs.sun.com/app/docs/doc/817-6223/6mlkidlhm?a=view>
     * <tt>exit()</tt></a> in the <b>Special Actions</b> section of the
     * <b>Actions and Subroutines</b> chapter of the <i>Solaris Dynamic
     * Tracing Guide</i>) or after the completion of all target
     * processes, or terminated abnormally because of an exception.  It
     * is necessary to call {@link Consumer#close()} to release any
     * system resources still held by the stopped consumer.
     *
     * @see #consumerStarted(ConsumerEvent e)
     */
    public void consumerStopped(ConsumerEvent e);

    /**
     * Called when the source {@link Consumer} wakes up to process its
     * buffer of traced probe data.
     *
     * @see #intervalEnded(ConsumerEvent e)
     */
    public void intervalBegan(ConsumerEvent e);

    /**
     * Called when the source {@link Consumer} finishes processing its
     * buffer of traced probe data and is about to sleep until the next
     * interval.  The rate of consumption may be controlled with the
     * {@link Option#switchrate switchrate} and {@link Option#aggrate
     * aggrate} options (see {@link Consumer#setOption(String option,
     * String value)}).
     *
     * @see #intervalBegan(ConsumerEvent e)
     */
    public void intervalEnded(ConsumerEvent e);
}