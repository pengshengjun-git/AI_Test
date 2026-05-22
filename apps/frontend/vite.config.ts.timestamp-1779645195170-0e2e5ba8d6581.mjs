// vite.config.ts
import { defineConfig } from "file:///F:/%E8%87%AA%E6%88%91%E6%96%87%E4%BB%B6/AI-Test-Platform/apps/frontend/node_modules/vite/dist/node/index.js";
import vue from "file:///F:/%E8%87%AA%E6%88%91%E6%96%87%E4%BB%B6/AI-Test-Platform/apps/frontend/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import tailwindcss from "file:///F:/%E8%87%AA%E6%88%91%E6%96%87%E4%BB%B6/AI-Test-Platform/apps/frontend/node_modules/@tailwindcss/vite/dist/index.mjs";
import { fileURLToPath, URL } from "node:url";
var __vite_injected_original_import_meta_url = "file:///F:/%E8%87%AA%E6%88%91%E6%96%87%E4%BB%B6/AI-Test-Platform/apps/frontend/vite.config.ts";
var vite_config_default = defineConfig({
  plugins: [vue(), tailwindcss()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  },
  server: {
    port: 80,
    host: "0.0.0.0",
    proxy: {
      "/api/v1/auth": {
        target: "http://dev-auth-service:8001",
        changeOrigin: true
      },
      "/api/v1/users": {
        target: "http://dev-user-service:8002",
        changeOrigin: true
      },
      "/api/v1/projects": {
        target: "http://dev-project-service:8003",
        changeOrigin: true
      },
      "/api/v1/testcases": {
        target: "http://dev-testcase-service:8004",
        changeOrigin: true
      },
      "/api/v1/defects": {
        target: "http://dev-defect-service:8005",
        changeOrigin: true
      },
      "/api/v1/requirements": {
        target: "http://dev-requirement-service:8006",
        changeOrigin: true
      },
      "/api/v1/dashboard": {
        target: "http://dev-dashboard-service:8007",
        changeOrigin: true
      },
      "/api/v1/strategy": {
        target: "http://dev-strategy-service:8008",
        changeOrigin: true
      },
      "/api/v1/ai": {
        target: "http://dev-ai-service:8010",
        changeOrigin: true
      },
      "/api/users": {
        target: "http://dev-user-service:8002",
        changeOrigin: true
      },
      "/api/roles": {
        target: "http://dev-user-service:8002",
        changeOrigin: true
      },
      "/api/departments": {
        target: "http://dev-user-service:8002",
        changeOrigin: true
      },
      "/api/menus": {
        target: "http://dev-user-service:8002",
        changeOrigin: true
      }
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxcdTgxRUFcdTYyMTFcdTY1ODdcdTRFRjZcXFxcQUktVGVzdC1QbGF0Zm9ybVxcXFxhcHBzXFxcXGZyb250ZW5kXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJGOlxcXFxcdTgxRUFcdTYyMTFcdTY1ODdcdTRFRjZcXFxcQUktVGVzdC1QbGF0Zm9ybVxcXFxhcHBzXFxcXGZyb250ZW5kXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi8lRTglODclQUElRTYlODglOTElRTYlOTYlODclRTQlQkIlQjYvQUktVGVzdC1QbGF0Zm9ybS9hcHBzL2Zyb250ZW5kL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSdcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xuaW1wb3J0IHRhaWx3aW5kY3NzIGZyb20gJ0B0YWlsd2luZGNzcy92aXRlJ1xuaW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAnbm9kZTp1cmwnXG5cbi8qKlxuICogVml0ZVx1OTE0RFx1N0Y2RVx1NjU4N1x1NEVGNlxuICovXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICBwbHVnaW5zOiBbdnVlKCksIHRhaWx3aW5kY3NzKCldLFxuICByZXNvbHZlOiB7XG4gICAgYWxpYXM6IHtcbiAgICAgICdAJzogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpXG4gICAgfVxuICB9LFxuICBzZXJ2ZXI6IHtcbiAgICBwb3J0OiA4MCxcbiAgICBob3N0OiAnMC4wLjAuMCcsXG4gICAgcHJveHk6IHtcbiAgICAgICcvYXBpL3YxL2F1dGgnOiB7XG4gICAgICAgIHRhcmdldDogJ2h0dHA6Ly9kZXYtYXV0aC1zZXJ2aWNlOjgwMDEnLFxuICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWVcbiAgICAgIH0sXG4gICAgICAnL2FwaS92MS91c2Vycyc6IHtcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2Rldi11c2VyLXNlcnZpY2U6ODAwMicsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3YxL3Byb2plY3RzJzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LXByb2plY3Qtc2VydmljZTo4MDAzJyxcbiAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlXG4gICAgICB9LFxuICAgICAgJy9hcGkvdjEvdGVzdGNhc2VzJzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LXRlc3RjYXNlLXNlcnZpY2U6ODAwNCcsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3YxL2RlZmVjdHMnOiB7XG4gICAgICAgIHRhcmdldDogJ2h0dHA6Ly9kZXYtZGVmZWN0LXNlcnZpY2U6ODAwNScsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3YxL3JlcXVpcmVtZW50cyc6IHtcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2Rldi1yZXF1aXJlbWVudC1zZXJ2aWNlOjgwMDYnLFxuICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWVcbiAgICAgIH0sXG4gICAgICAnL2FwaS92MS9kYXNoYm9hcmQnOiB7XG4gICAgICAgIHRhcmdldDogJ2h0dHA6Ly9kZXYtZGFzaGJvYXJkLXNlcnZpY2U6ODAwNycsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3YxL3N0cmF0ZWd5Jzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LXN0cmF0ZWd5LXNlcnZpY2U6ODAwOCcsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3YxL2FpJzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LWFpLXNlcnZpY2U6ODAxMCcsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL3VzZXJzJzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LXVzZXItc2VydmljZTo4MDAyJyxcbiAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlXG4gICAgICB9LFxuICAgICAgJy9hcGkvcm9sZXMnOiB7XG4gICAgICAgIHRhcmdldDogJ2h0dHA6Ly9kZXYtdXNlci1zZXJ2aWNlOjgwMDInLFxuICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWVcbiAgICAgIH0sXG4gICAgICAnL2FwaS9kZXBhcnRtZW50cyc6IHtcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2Rldi11c2VyLXNlcnZpY2U6ODAwMicsXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgfSxcbiAgICAgICcvYXBpL21lbnVzJzoge1xuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vZGV2LXVzZXItc2VydmljZTo4MDAyJyxcbiAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlXG4gICAgICB9XG4gICAgfVxuICB9XG59KSJdLAogICJtYXBwaW5ncyI6ICI7QUFBOFUsU0FBUyxvQkFBb0I7QUFDM1csT0FBTyxTQUFTO0FBQ2hCLE9BQU8saUJBQWlCO0FBQ3hCLFNBQVMsZUFBZSxXQUFXO0FBSDBKLElBQU0sMkNBQTJDO0FBUTlPLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLFNBQVMsQ0FBQyxJQUFJLEdBQUcsWUFBWSxDQUFDO0FBQUEsRUFDOUIsU0FBUztBQUFBLElBQ1AsT0FBTztBQUFBLE1BQ0wsS0FBSyxjQUFjLElBQUksSUFBSSxTQUFTLHdDQUFlLENBQUM7QUFBQSxJQUN0RDtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFFBQVE7QUFBQSxJQUNOLE1BQU07QUFBQSxJQUNOLE1BQU07QUFBQSxJQUNOLE9BQU87QUFBQSxNQUNMLGdCQUFnQjtBQUFBLFFBQ2QsUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLE1BQ2hCO0FBQUEsTUFDQSxpQkFBaUI7QUFBQSxRQUNmLFFBQVE7QUFBQSxRQUNSLGNBQWM7QUFBQSxNQUNoQjtBQUFBLE1BQ0Esb0JBQW9CO0FBQUEsUUFDbEIsUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLE1BQ2hCO0FBQUEsTUFDQSxxQkFBcUI7QUFBQSxRQUNuQixRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsTUFDaEI7QUFBQSxNQUNBLG1CQUFtQjtBQUFBLFFBQ2pCLFFBQVE7QUFBQSxRQUNSLGNBQWM7QUFBQSxNQUNoQjtBQUFBLE1BQ0Esd0JBQXdCO0FBQUEsUUFDdEIsUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLE1BQ2hCO0FBQUEsTUFDQSxxQkFBcUI7QUFBQSxRQUNuQixRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsTUFDaEI7QUFBQSxNQUNBLG9CQUFvQjtBQUFBLFFBQ2xCLFFBQVE7QUFBQSxRQUNSLGNBQWM7QUFBQSxNQUNoQjtBQUFBLE1BQ0EsY0FBYztBQUFBLFFBQ1osUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLE1BQ2hCO0FBQUEsTUFDQSxjQUFjO0FBQUEsUUFDWixRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsTUFDaEI7QUFBQSxNQUNBLGNBQWM7QUFBQSxRQUNaLFFBQVE7QUFBQSxRQUNSLGNBQWM7QUFBQSxNQUNoQjtBQUFBLE1BQ0Esb0JBQW9CO0FBQUEsUUFDbEIsUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLE1BQ2hCO0FBQUEsTUFDQSxjQUFjO0FBQUEsUUFDWixRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsTUFDaEI7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
