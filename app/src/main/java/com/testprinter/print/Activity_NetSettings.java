/**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.brother.ptouch.sdk.PrinterInfo.PrinterSettingItem;
import com.testprinter.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Activity_NetSettings extends BasePrinterSettingActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_cutome_layout);
        addPreferencesFromResource(R.xml.pj7_net_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateValue();

        mList = Arrays.asList(PrinterSettingItem.NET_BOOTMODE,
                PrinterSettingItem.NET_INTERFACE,
                PrinterSettingItem.NET_IPV4_BOOTMETHOD,
                PrinterSettingItem.NET_STATIC_IPV4ADDRESS,
                PrinterSettingItem.NET_SUBNETMASK,
                PrinterSettingItem.NET_GATEWAY,
                PrinterSettingItem.NET_DNS_IPV4_BOOTMETHOD,
                PrinterSettingItem.NET_PRIMARY_DNS_IPV4ADDRESS,
                PrinterSettingItem.NET_SECOND_DNS_IPV4ADDRESS,
                PrinterSettingItem.NET_NODENAME,
                PrinterSettingItem.WIRESSDIRECT_KEY_CREATE_MODE,
                PrinterSettingItem.WIRESSDIRECT_SSID,
                PrinterSettingItem.WIRESSDIRECT_NETWORK_KEY);

    }

    private void updateValue() {

        setPreferenceValue("net_bootmode");
        setPreferenceValue("net_interface");
        setPreferenceValue("net_ip4_staticmode");
        setEditValue("net_ipv4_static_address");
        setEditValue("net_subnetmask");
        setEditValue("net_gateway");
        setPreferenceValue("net_dns_ipv4_bootmethod");
        setEditValue("net_primary_dns_ipv4address");
        setEditValue("net_second_dns_ipv4address");
        setEditValue("net_nodenam");
        setPreferenceValue("wirelessdirect_key_create_mode");
        setEditValue("wirelessdirect_ssid");
        setEditValue("wirelessdirect_network_key");
    }

    protected Map<PrinterSettingItem, String> createSettingsMap() {

        Map<PrinterSettingItem, String> settings = new HashMap<PrinterSettingItem, String>();

        settings.put(PrinterSettingItem.NET_BOOTMODE,
                sharedPreferences.getString("net_bootmode", ""));

        settings.put(PrinterSettingItem.NET_INTERFACE,
                sharedPreferences.getString("net_interface", ""));

        settings.put(PrinterSettingItem.NET_IPV4_BOOTMETHOD,
                sharedPreferences.getString("net_ip4_staticmode", ""));
        settings.put(PrinterSettingItem.NET_STATIC_IPV4ADDRESS,
                sharedPreferences.getString("net_ipv4_static_address", ""));

        settings.put(PrinterSettingItem.NET_SUBNETMASK,
                sharedPreferences.getString("net_subnetmask", ""));
        settings.put(PrinterSettingItem.NET_GATEWAY,
                sharedPreferences.getString("net_gateway", ""));
        settings.put(PrinterSettingItem.NET_DNS_IPV4_BOOTMETHOD,
                sharedPreferences.getString("net_dns_ipv4_bootmethod", ""));
        settings.put(PrinterSettingItem.NET_PRIMARY_DNS_IPV4ADDRESS,
                sharedPreferences.getString("net_primary_dns_ipv4address", ""));
        settings.put(PrinterSettingItem.NET_SECOND_DNS_IPV4ADDRESS,
                sharedPreferences.getString("net_second_dns_ipv4address", ""));
        settings.put(PrinterSettingItem.NET_NODENAME,
                sharedPreferences.getString("net_nodenam", ""));
        settings.put(PrinterSettingItem.WIRESSDIRECT_KEY_CREATE_MODE,
                sharedPreferences.getString("wirelessdirect_key_create_mode",
                        ""));
        settings.put(PrinterSettingItem.WIRESSDIRECT_SSID,
                sharedPreferences.getString("wirelessdirect_ssid", ""));
        settings.put(PrinterSettingItem.WIRESSDIRECT_NETWORK_KEY,
                sharedPreferences.getString("wirelessdirect_network_key", ""));

        return settings;

    }

    protected void saveSettings(Map<PrinterSettingItem, String> settings) {

        for (PrinterSettingItem str : settings.keySet()) {
            switch (str) {
                case NET_BOOTMODE:
                    setPreferenceValue("net_bootmode", settings.get(str));
                    break;
                case NET_INTERFACE:
                    setPreferenceValue("net_interface", settings.get(str));
                    break;
                case NET_IPV4_BOOTMETHOD:
                    setPreferenceValue("net_ip4_staticmode", settings.get(str));
                    break;
                case NET_STATIC_IPV4ADDRESS:
                    setEditValue("net_ipv4_static_address", settings.get(str));
                    break;
                case NET_SUBNETMASK:
                    setEditValue("net_subnetmask", settings.get(str));
                    break;
                case NET_GATEWAY:
                    setEditValue("net_gateway", settings.get(str));
                    break;
                case NET_DNS_IPV4_BOOTMETHOD:
                    setPreferenceValue("net_dns_ipv4_bootmethod", settings.get(str));
                    break;
                case NET_PRIMARY_DNS_IPV4ADDRESS:
                    setEditValue("net_primary_dns_ipv4address", settings.get(str));
                    break;
                case NET_SECOND_DNS_IPV4ADDRESS:
                    setEditValue("net_second_dns_ipv4address", settings.get(str));
                    break;
                case NET_NODENAME:
                    setEditValue("net_nodenam", settings.get(str));
                    break;
                case WIRESSDIRECT_KEY_CREATE_MODE:
                    setPreferenceValue("wirelessdirect_key_create_mode",
                            settings.get(str));
                    break;
                case WIRESSDIRECT_SSID:
                    setEditValue("wirelessdirect_ssid", settings.get(str));
                    break;
                case WIRESSDIRECT_NETWORK_KEY:
                    setEditValue("wirelessdirect_network_key", settings.get(str));
                    break;
                default:
                    break;
            }
        }

    }

}
