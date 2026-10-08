package com.springboot.demo.filter;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author kefei.yan
 * @date 2026/9/29
 */

public class CachingResponseWrapper extends ContentCachingResponseWrapper {

    private final HttpServletResponse rawResponse;
    private Boolean streaming = null;
    private TeeOutputStream teeOutputStream;
    private ServletOutputStream teeServletOutputStream;
    private boolean outputStreamRetrieved = false;
    private boolean writerRetrieved = false;

    public CachingResponseWrapper(HttpServletResponse response) {
        super(response);
        this.rawResponse = response;
    }

    @Override
    public void setContentType(String type) {
        resolveStreamingMode(type);
        super.setContentType(type);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writerRetrieved) {
            throw new IllegalStateException("getWriter has already been called on this response");
        }
        outputStreamRetrieved = true;
        resolveStreamingMode(getContentType());
        if (isStreaming()) {
            return getTeeServletOutputStream();
        }
        return super.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStreamRetrieved) {
            throw new IllegalStateException("getOutputStream has already been called on this response");
        }
        writerRetrieved = true;
        return super.getWriter();
    }

    @Override
    public void sendError(int sc) throws IOException {
        rawResponse.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        rawResponse.sendError(sc, msg);
    }

    @Override
    public void flushBuffer() throws IOException {
        if (isStreaming()) {
            rawResponse.flushBuffer();
        } else {
            super.flushBuffer();
        }
    }

    @Override
    public void copyBodyToResponse() throws IOException {
        if (!isStreaming()) {
            super.copyBodyToResponse();
        }
    }

    private boolean isStreaming() {
        return Boolean.TRUE.equals(streaming);
    }

    private ServletOutputStream getTeeServletOutputStream() {
        return teeServletOutputStream;
    }

    private void resolveStreamingMode(String contentType) {
        if (streaming != null) {
            return;
        }
        if (contentType == null) {
            return;
        }
        String lower = contentType.toLowerCase();
        if (isBinaryContentType(lower)) {
            try {
                streaming = true;
                teeOutputStream = new TeeOutputStream(rawResponse.getOutputStream());
                teeServletOutputStream = new TeeServletOutputStream(teeOutputStream);
            } catch (IOException e) {
                streaming = false;
            }
        } else {
            streaming = false;
        }
    }

    private boolean isBinaryContentType(String lowerContentType) {
        return lowerContentType.startsWith("application/octet-stream")
                || lowerContentType.startsWith("application/vnd.openxmlformats-officedocument")
                || lowerContentType.startsWith("application/vnd.ms-excel")
                || lowerContentType.startsWith("application/zip")
                || lowerContentType.startsWith("application/x-download")
                || lowerContentType.startsWith("application/x-msdownload")
                || lowerContentType.startsWith("application/pdf")
                || lowerContentType.startsWith("application/msword")
                || lowerContentType.startsWith("application/vnd.ms-officedocument")
                || lowerContentType.startsWith("application/x-rar-compressed")
                || lowerContentType.startsWith("application/x-7z-compressed")
                || lowerContentType.startsWith("application/gzip")
                || lowerContentType.startsWith("image/")
                || lowerContentType.startsWith("audio")
                || lowerContentType.startsWith("video");
    }

    private static class TeeOutputStream extends OutputStream {
        private final OutputStream outputStream;
        private boolean closed = false;

        public TeeOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            if (closed) {
                return;
            }
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (closed) {
                return;
            }
            outputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            if (closed) {
                return;
            }
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            if (closed) {
                return;
            }
            closed = true;
            try {
                outputStream.flush();
            } finally {
                outputStream.close();
            }
        }
    }

    private static class TeeServletOutputStream extends ServletOutputStream {
        private final TeeOutputStream teeOutputStream;

        public TeeServletOutputStream(TeeOutputStream teeOutputStream) {
            this.teeOutputStream = teeOutputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(int b) throws IOException {
            teeOutputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            teeOutputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            teeOutputStream.flush();
        }

        @Override
        public void close() throws IOException {
            teeOutputStream.close();
        }
    }
}
