package cn.com.project.common;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtil {
	private static final String[] HEADERS_ABOUT_CLIENT_IP = {
		"X-Forwarded-For",
		"Proxy-Client-IP",// Apache(Weblogic Plug-In Enable)+WebLogic 搭配
		"WL-Proxy-Client-IP",// Apache(Weblogic Plug-In Enable)+WebLogic 搭配
		"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",// ng配置
														// proxy_set_header
														// HTTP_CLIENT_IP
														// $remote_addr; 才有用
		"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
    /**
     * 获取本机ip地址
     */
    public static String getIpAddress() {
      try {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
          NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
          if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
            continue;
          } else {
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
              ip = addresses.nextElement();
              if (ip != null && ip instanceof Inet4Address) {
//                  System.out.println(ip.getHostAddress());
            	  return ip.getHostAddress();
              }
            }
          }
        }
      } catch (Exception e) {
        //logger.error("IP地址获取失败", e);
    	  e.printStackTrace();
      }
      return "";
    }

	/**
	 * 获取客户端ip 方法1
	 * @param request
	 * @return
	 */
	public static String getRequestRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.contains(",")) {
			ip = ip.split(",")[0];
		}

		if (!checkIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private static boolean checkIp(String ip) {
		if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取客户端ip 方法2
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		for (String header : HEADERS_ABOUT_CLIENT_IP) {
			String ip = request.getHeader(header);
			if (!checkIp(ip)) {
				// return ip;
				// X-Forwarded-For: client1, proxy1, proxy2
				String[] ips = ip.split(",");
				return ips[0];
			}
		}
		return request.getRemoteAddr();
	} 
    public static void main(String[] args) throws Exception {
    	System.out.println(getIpAddress());
    }
}
