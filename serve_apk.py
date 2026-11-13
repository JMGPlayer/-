import http.server
import socketserver
import os
import urllib.parse

PORT = 3000
DIRECTORY = "/app/applet/public"
APK_PATH = "/app/applet/CarValueCoach_v1.5.apk"
ZIP_PATH = "/app/applet/CarValueCoach_v1.5.zip"

APK_ROUTES = [
    '/CarValueCoach_v1.5.apk',
    '/CarValueCoach_v1.4.apk',
    '/CarValueCoach_v1.3.apk',
    '/CarValueCoach_v1.2.apk',
    '/car_value_coach.apk',
    '/download',
    '/apk',
    '/app.apk',
    '/latest.apk',
    '/app-debug.apk',
]
ZIP_ROUTES = [
    '/CarValueCoach_v1.5.zip',
    '/CarValueCoach_v1.4.zip',
    '/CarValueCoach_v1.3.zip',
    '/CarValueCoach_v1.2.zip',
    '/car_value_coach.zip',
    '/zip',
]

class APKDownloadHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        parsed_path = urllib.parse.urlparse(self.path).path
        if parsed_path in APK_ROUTES:
            self.send_file(APK_PATH, "application/vnd.android.package-archive", "CarValueCoach_v1.5.apk")
            return
        if parsed_path in ZIP_ROUTES:
            self.send_file(ZIP_PATH, "application/zip", "CarValueCoach_v1.5.zip")
            return
        super().do_GET()

    def do_HEAD(self):
        parsed_path = urllib.parse.urlparse(self.path).path
        if parsed_path in APK_ROUTES:
            self.send_file_headers(APK_PATH, "application/vnd.android.package-archive", "CarValueCoach_v1.5.apk")
            return
        if parsed_path in ZIP_ROUTES:
            self.send_file_headers(ZIP_PATH, "application/zip", "CarValueCoach_v1.5.zip")
            return
        super().do_HEAD()

    def send_file_headers(self, target_path, content_type, filename):
        if not os.path.exists(target_path):
            fallback = os.path.join(DIRECTORY, filename)
            if os.path.exists(fallback):
                target_path = fallback
            else:
                self.send_error(404, "File not found")
                return None, 0
        file_size = os.path.getsize(target_path)
        self.send_response(200)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Disposition", f'attachment; filename="{filename}"')
        self.send_header("Content-Length", str(file_size))
        self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Accept-Ranges", "bytes")
        self.end_headers()
        return target_path, file_size

    def send_file(self, target_path, content_type, filename):
        path, size = self.send_file_headers(target_path, content_type, filename)
        if not path or size == 0:
            return
        try:
            with open(path, 'rb') as f:
                while chunk := f.read(1024 * 1024):  # 1MB chunk
                    try:
                        self.wfile.write(chunk)
                    except (BrokenPipeError, ConnectionResetError):
                        break
        except Exception as e:
            print(f"Error serving APK: {e}")

class ReusableTCPServer(socketserver.TCPServer):
    allow_reuse_address = True

if __name__ == "__main__":
    if not os.path.exists(DIRECTORY):
        os.makedirs(DIRECTORY, exist_ok=True)
    os.chdir(DIRECTORY)
    with ReusableTCPServer(("", PORT), APKDownloadHandler) as httpd:
        print(f"Serving Car Value Coach APK on port {PORT}...")
        httpd.serve_forever()
