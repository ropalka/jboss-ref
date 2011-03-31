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

import static org.junit.Assert.assertSame;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.ref.Reference;
import org.jboss.ref.WeakReference;
import org.jboss.ref.Reference.Type;
import org.jboss.ref.util.TestReaper;
import org.junit.Test;

/**
 * Test for {@link WeakReference}.
 * 
 * @author <a href="mailto:flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class WeakReferenceTestCase extends AbstractReapableReferenceTest {

    @Test
    public void plainWeakReference() {
        final Reference<String, String> reference = new WeakReference<String, String>("referent", "attachment");
        assertReference(reference, "referent", "attachment", null);
    }

    @Test
    public void weakReferenceWithoutAttachment() {
        final Reference<String, String> reference = new WeakReference<String, String>("referent");
        assertReference(reference, "referent", null, null);
    }

    @Test
    public void nullWeakReference() throws Exception {
        final Reference<Thing, Integer> reference = new WeakReference<Thing, Integer>(null, Integer.valueOf(0));
        assertReference(reference, null, Integer.valueOf(0));
    }

    @Test
    public void weakReferenceWithReferenceQueue() throws Exception {
        final ReferenceQueue<Collection<Object>> referenceQueue = new ReferenceQueue<Collection<Object>>();
        Collection<Object> collection = new ArrayList<Object>();
        final Reference<Collection<Object>, String> reference = new WeakReference<Collection<Object>, String>(collection, "collection", referenceQueue);
        assertReference(reference, collection, "collection", null);
        collection = null;
        System.gc();
        assertSame(reference, referenceQueue.remove(300));
    }

    @Test
    public void weakReferenceWithReaper() throws Exception {
        Thing thing = new Thing();
        final TestReaper<Thing, Void> reaper = new TestReaper<Thing, Void>();
        final Reference<Thing, Void> reference = new WeakReference<Thing, Void>(thing, null, reaper);
        assertReference(reference, thing, null, reaper);
        thing = null;
        System.gc();
        assertSame(reference, reaper.getReapedReference());
    }

    @Override
    <T, A> Reference<T, A> createReference(T value, A attachment) {
        return new WeakReference<T, A>(value, attachment);
    }

    @Override
    Type getTestedReferenceType() {
        return Type.WEAK;
    }
}