/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.ref;

import java.lang.ref.ReferenceQueue;

/**
 * A reapable phantom reference with an attachment.  If a {@link Reaper} is given, then it will be used to asynchronously
 * clean up the referent.
 *
 * @param <T> the reference value type
 * @param <A> the attachment type
 *
 * @see java.lang.ref.PhantomReference
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class PhantomReference<T, A> extends java.lang.ref.PhantomReference<T> implements Reference<T, A>, Reapable<T, A> {
    private final A attachment;
    private final Reaper<T, A> reaper;

    public PhantomReference(final T referent, final A attachment, final ReferenceQueue<? super T> q) {
        super(referent, q);
        this.attachment = attachment;
        reaper = null;
    }

    public PhantomReference(final T referent, final A attachment, final Reaper<T, A> reaper) {
        super(referent, References.ReaperThread.REAPER_QUEUE);
        this.reaper = reaper;
        this.attachment = attachment;
    }

    public A getAttachment() {
        return attachment;
    }

    public Type getType() {
        return Type.PHANTOM;
    }

    public Reaper<T, A> getReaper() {
        return reaper;
    }

    public String toString() {
        return "phantom reference to " + String.valueOf(get());
    }
}
