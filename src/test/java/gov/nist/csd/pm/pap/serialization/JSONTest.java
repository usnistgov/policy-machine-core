package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;

public class JSONTest {

    String json = """
            {
              "graph": {
                "resourceAccessRights": [
                  "read",
                  "write"
                ],
                "policyClasses": [
                  {
                    "name": "pm_admin:policy",
                    "objectAttributes": [
                      {
                        "name": "pm_admin:POLICY_CLASSES",
                        "children": [
                          {
                            "name": "pm_admin:policy:target"
                          },
                          {
                            "name": "pc1:target"
                          },
                          {
                            "name": "pc2:target"
                          },
                          {
                            "name": "super_policy:target"
                          }
                        ]
                      },
                      {
                        "name": "pm_admin:FUNCTIONS"
                      },
                      {
                        "name": "pm_admin:CONSTANTS"
                      },
                      {
                        "name": "pm_admin:OBLIGATIONS"
                      },
                      {
                        "name": "pm_admin:PROHIBITIONS"
                      }
                    ]
                  },
                  {
                    "name": "super_policy",
                    "userAttributes": [
                      {
                        "name": "super_ua"
                      },
                      {
                        "name": "super_ua1"
                      }
                    ],
                    "associations": {
                      "super_ua": [
                        {
                          "target": "super_ua1",
                          "arset": [
                            "*"
                          ]
                        },
                        {
                          "target": "pm_admin:POLICY_CLASSES",
                          "arset": [
                            "*"
                          ]
                        },
                        {
                          "target": "pm_admin:CONSTANTS",
                          "arset": [
                            "*"
                          ]
                        },
                        {
                          "target": "pm_admin:policy:target",
                          "arset": [
                            "*"
                          ]
                        },
                        {
                          "target": "pm_admin:FUNCTIONS",
                          "arset": [
                            "*"
                          ]
                        }
                      ]
                    }
                  },
                  {
                    "name": "pc1",
                    "userAttributes": [
                      {
                        "name": "ua1"
                      }
                    ],
                    "objectAttributes": [
                      {
                        "name": "oa1"
                      }
                    ],
                    "associations": {
                      "ua1": [
                        {
                          "target": "oa1",
                          "arset": [
                            "read",
                            "create_policy_class",
                            "write"
                          ]
                        }
                      ]
                    }
                  },
                  {
                    "name": "pc2",
                    "userAttributes": [
                      {
                        "name": "ua2"
                      }
                    ],
                    "objectAttributes": [
                      {
                        "name": "oa2"
                      }
                    ],
                    "associations": {
                      "ua2": [
                        {
                          "target": "oa2",
                          "arset": [
                            "read",
                            "write"
                          ]
                        }
                      ]
                    }
                  }
                ],
                "users": [
                  {
                    "name": "super",
                    "parents": [
                      "super_ua",
                      "super_ua1"
                    ]
                  },
                  {
                    "name": "u1",
                    "parents": [
                      "ua1",
                      "ua2"
                    ]
                  },
                  {
                    "name": "u2",
                    "parents": [
                      "ua1",
                      "ua2"
                    ]
                  }
                ],
                "objects": [
                  {
                    "name": "o1",
                    "parents": [
                      "oa1",
                      "oa2"
                    ]
                  }
                ]
              },
              "prohibitions": [
                {
                  "name": "u2-prohibition",
                  "subject": {
                    "name": "u2",
                    "type": "USER"
                  },
                  "containers": [
                    {
                      "name": "oa1",
                      "complement": false
                    },
                    {
                      "name": "oa2",
                      "complement": false
                    }
                  ],
                  "accessRightSet": [
                    "write"
                  ],
                  "intersection": true
                }
              ],
              "obligations": [
                "create obligation 'o1-obligation' {create rule 'o1-assignment-rule' when any user performs ['assign'] on 'o1' do (evtCtx) {let parent = evtCtx['parent']associate 'ua1' and parent with ['read', 'write']associate 'ua2' and parent with ['read', 'write']}}"
              ],
              "userDefinedPML": {
                "functions": {
                  "f1": "function f1() string {return 'hello world'}",
                  "f2": "function f2(string s) string {return s}"
                },
                "constants": {
                  "C3": "constant1",
                  "C1": "constant1",
                  "C2": "constant1"
                }
              }
            }
            """;

    @Test
    void testSerialization() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext(SUPER_USER), json, new JSONDeserializer());

        String serialize = pap.serialize(new JSONSerializer());

        PAP pap2 = new PAP(new MemoryPolicyStore());
        pap2.deserialize(new UserContext(SUPER_USER), serialize, new JSONDeserializer());
    }

}
