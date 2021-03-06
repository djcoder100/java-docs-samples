/**
 * Copyright 2017, Google, Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cloud.iot.examples;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/** Command line options for the Device Manager example. */
public class DeviceRegistryExampleOptions {
  String projectId;
  String ecPublicKeyFile = "ec_public.pem";
  String rsaCertificateFile = "rsa_cert.pem";
  String cloudRegion = "us-central1";
  String command = "help";
  String deviceId; // Default to UUID?
  String pubsubTopic;
  String registryName;

  /** Construct an DeviceRegistryExampleOptions class from command line flags. */
  public static DeviceRegistryExampleOptions fromFlags(String[] args) {
    Options options = new Options();
    // Required arguments
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("pubsub_topic")
            .hasArg()
            .desc("Pub/Sub topic to create registry in.")
            .required()
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("command")
            .hasArg()
            .desc(
                "Command to run:"
                + "\n\tcreate-iot-topic" // TODO: Descriptions or too verbose?
                + "\n\tcreate-rsa"
                + "\n\tcreate-es"
                + "\n\tcreate-unauth"
                + "\n\tcreate-registry"
                + "\n\tdelete-device"
                + "\n\tdelete-registry"
                + "\n\tget-device"
                + "\n\tget-device-state"
                + "\n\tget-registry"
                + "\n\tlist-devices"
                + "\n\tlist-registries"
                + "\n\tpatch-device-es"
                + "\n\tpatch-device-rsa")
            .required()
            .build());

    // Optional arguments.
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("ec_public_key_file")
            .hasArg()
            .desc("Path to ES256 public key file.")
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("rsa_certificate_file")
            .hasArg()
            .desc("Path to RS256 certificate file.")
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("cloud_region")
            .hasArg()
            .desc("GCP cloud region.")
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("project_id")
            .hasArg()
            .desc("GCP cloud project name.")
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("registry_name")
            .hasArg()
            .desc("Name for your Device Registry.")
            .build());
    options.addOption(
        Option.builder()
            .type(String.class)
            .longOpt("device_id")
            .hasArg()
            .desc("Name for your Device.")
            .build());

    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine;
    try {
      commandLine = parser.parse(options, args);
      DeviceRegistryExampleOptions res = new DeviceRegistryExampleOptions();

      res.command = commandLine.getOptionValue("command");

      if (res.command.equals("help") || res.command.equals("")) {
        throw new ParseException("Invalid command, showing help.");
      }

      if (commandLine.hasOption("project_id")) {
        res.projectId = commandLine.getOptionValue("project_id");
      } else {
        try {
          res.projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
        } catch (NullPointerException npe) {
          res.projectId = System.getenv("GCLOUD_PROJECT");
        }
      }

      if (commandLine.hasOption("pubsub_topic")) {
        res.pubsubTopic = commandLine.getOptionValue("pubsub_topic");
      } else {
        // TODO: Get from environment variable
      }

      if (commandLine.hasOption("ec_public_key_file")) {
        res.ecPublicKeyFile = commandLine.getOptionValue("ec_public_key_file");
      }
      if (commandLine.hasOption("rsa_certificate_file")) {
        res.rsaCertificateFile = commandLine.getOptionValue("rsa_certificate_file");
      }
      if (commandLine.hasOption("cloud_region")) {
        res.cloudRegion = commandLine.getOptionValue("cloud_region");
      }
      if (commandLine.hasOption("registry_name")) {
        res.registryName = commandLine.getOptionValue("registry_name");
      }
      if (commandLine.hasOption("device_id")) {
        res.deviceId = commandLine.getOptionValue("device_id");
      }
      return res;
    } catch (ParseException e) {
      String header = "Cloud IoT Core Commandline Example (Device / Registry management): \n\n";
      String footer = "\nhttps://cloud.google.com/iot-core";

      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("DeviceRegistryExample", header, options, footer,
          true);

      System.err.println(e.getMessage());
      return null;
    }
  }
}