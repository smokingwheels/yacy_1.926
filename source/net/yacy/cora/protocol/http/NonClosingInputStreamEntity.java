/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package net.yacy.cora.protocol.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

/**
 * This is InputStreamEntity from httpcore-4.1
 * In httpcore-4.1.1 InputStreamEntity finally closes its InputStream.
 * In general that is a good idea - but while in use of our proxy doing a POST,
 * this also closes the server-socket an the proxy-client doesn't get anything.
 */
public class NonClosingInputStreamEntity extends AbstractHttpEntity {

    private final static int BUFFER_SIZE = 2048;

    private final InputStream content;
    private final long length;

    public NonClosingInputStreamEntity(final InputStream instream, final long length) {
        super();
        if (instream == null) {
            throw new IllegalArgumentException("Source input stream may not be null");
        }
        this.content = instream;
        this.length = length;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return this.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.content;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        final InputStream instream = this.content;
        final byte[] buffer = new byte[BUFFER_SIZE];
        int l;
        if (this.length < 0) {
            // consume until EOF
            while ((l = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, l);
            }
        } else {
            // consume no more than length
            long remaining = this.length;
            while (remaining > 0) {
                l = instream.read(buffer, 0, (int)Math.min(BUFFER_SIZE, remaining));
                if (l == -1) {
                    break;
                }
                outstream.write(buffer, 0, l);
                remaining -= l;
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

    /**
     * @deprecated Either use {@link #getContent()} and call {@link java.io.InputStream#close()} on that;
     * otherwise call {@link #writeTo(OutputStream)} which is required to free the resources.
     */
    @Override
    @Deprecated
    public void consumeContent() throws IOException {
        // If the input stream is from a connection, closing it will read to
        // the end of the content. Otherwise, we don't care what it does.
        this.content.close();
    }

}
