# Changelog

## [0.10.1](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.10.0...v0.10.1) (2026-10-17)


### Bug Fixes

* **physic:** avoid deprecated code ([4878df2](https://github.com/OneLiteFeatherNET/Bounce/commit/4878df2b33e1ffddaf3f8bcf3a515666ef0b4c24))

## [0.10.0](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.9.0...v0.10.0) (2026-10-16)


### Features

* **map:** configure super jump multiplier per map ([#154](https://github.com/OneLiteFeatherNET/Bounce/issues/154)) ([7d64749](https://github.com/OneLiteFeatherNET/Bounce/commit/7d6474904b083ddfe5cc10225706b9e82c32dcc7))
* **powerup:** change rotation ([d6c2bfc](https://github.com/OneLiteFeatherNET/Bounce/commit/d6c2bfc8218b99dd7b0226e7fe84cdf0f85313c5))
* **powerup:** replace speed with slow falling and add gravity flip ([#153](https://github.com/OneLiteFeatherNET/Bounce/issues/153)) ([4a855c1](https://github.com/OneLiteFeatherNET/Bounce/commit/4a855c10c5768737aef1a83c1f2f695c63456647))
* **setup:** add bounce preview for push blocks ([#152](https://github.com/OneLiteFeatherNET/Bounce/issues/152)) ([e147785](https://github.com/OneLiteFeatherNET/Bounce/commit/e1477856a3cbfe251fc4c02150aca95f9925b1c3))


### Bug Fixes

* **setup:** cache pushIndex through layout rebuilds ([5f5c53c](https://github.com/OneLiteFeatherNET/Bounce/commit/5f5c53cd7fcdaffdda5d164ae05590ae976500ee))
* **setup:** use push value dialog in each case ([8c3b72a](https://github.com/OneLiteFeatherNET/Bounce/commit/8c3b72a12b58b29a41fed15c1bed92782facfe5e))

## [0.9.0](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.8.0...v0.9.0) (2026-10-16)


### Features

* **config:** configure Sentry through config.properties ([#147](https://github.com/OneLiteFeatherNET/Bounce/issues/147)) ([4a7ebc2](https://github.com/OneLiteFeatherNET/Bounce/commit/4a7ebc2b2cae1cf34edb517ea48ead9150262747))


### Bug Fixes

* **cloud:** improve shutdown handling ([a476054](https://github.com/OneLiteFeatherNET/Bounce/commit/a47605431370fb0f34e6ee21a7ca36724cd90784))
* **deps:** update dependency io.sentry:sentry to v8.61.0 ([#148](https://github.com/OneLiteFeatherNET/Bounce/issues/148)) ([7bee2c9](https://github.com/OneLiteFeatherNET/Bounce/commit/7bee2c955198c5e188aedba6334c1b5f4bf54e3a))
* **game:** ignore damagers who left the round ([5f5e868](https://github.com/OneLiteFeatherNET/Bounce/commit/5f5e868b7217958b6c5f99dc7cc13cea05ff7914))
* **item:** update held item slot ([a57f430](https://github.com/OneLiteFeatherNET/Bounce/commit/a57f430707b42e3cec969d27bbf32bb91be7260d))
* **player:** detect blocks under hitbox corners ([#149](https://github.com/OneLiteFeatherNET/Bounce/issues/149)) ([9aaf23d](https://github.com/OneLiteFeatherNET/Bounce/commit/9aaf23d0a12307738efabc58050c8af21f026bc4))

## [0.8.0](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.7.0...v0.8.0) (2026-10-15)


### Features

* **auth:** add velocity handling ([f4a5fb3](https://github.com/OneLiteFeatherNET/Bounce/commit/f4a5fb38dd1e83bed391b65ec3769813afa3158a))


### Bug Fixes

* **deps:** update dependency net.luckperms:api to v6 ([#107](https://github.com/OneLiteFeatherNET/Bounce/issues/107)) ([bab9f95](https://github.com/OneLiteFeatherNET/Bounce/commit/bab9f955c931c93e26ff047d19e2a03437ba8718))
* **deps:** update dependency net.luckperms:api to v6.0.2 ([#141](https://github.com/OneLiteFeatherNET/Bounce/issues/141)) ([784de12](https://github.com/OneLiteFeatherNET/Bounce/commit/784de121bcbef9049f6aad2c5b88d3350c4a8296))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.9 ([#143](https://github.com/OneLiteFeatherNET/Bounce/issues/143)) ([4252dde](https://github.com/OneLiteFeatherNET/Bounce/commit/4252dde6f1f0f8263280a1f1d5c06d2c08f5e026))
* **deps:** update dependency net.onelitefeather:falco-bom to v3.0.1 ([#142](https://github.com/OneLiteFeatherNET/Bounce/issues/142)) ([d1ac4e7](https://github.com/OneLiteFeatherNET/Bounce/commit/d1ac4e771f39e4c267d6adc4d331c20214903cd8))
* **deps:** update dependency net.onelitefeather:pica to v0.1.4 ([#140](https://github.com/OneLiteFeatherNET/Bounce/issues/140)) ([8c29b20](https://github.com/OneLiteFeatherNET/Bounce/commit/8c29b202ea5cec120378388e62c30d2cbec0842a))
* **deps:** update dependency net.onelitefeather:pica to v0.1.5 ([#144](https://github.com/OneLiteFeatherNET/Bounce/issues/144)) ([3df2a3c](https://github.com/OneLiteFeatherNET/Bounce/commit/3df2a3c882fd41be07aff71771da7247edf046f5))
* **deps:** update luckperms minestom-loader to 6.0.2 ([#139](https://github.com/OneLiteFeatherNET/Bounce/issues/139)) ([7359065](https://github.com/OneLiteFeatherNET/Bounce/commit/73590654f365cd3dbe3ce9bc55cc9ed8d886c676))
* **lobby:** reset force start when the countdown gets reset ([54251ce](https://github.com/OneLiteFeatherNET/Bounce/commit/54251ce9a153022966a480ba44d894202666ade7))
* **player:** keep pads usable after a push was blocked by the cooldown ([1b77ab2](https://github.com/OneLiteFeatherNET/Bounce/commit/1b77ab2a0824f5e00c207ac3857e768aee3af22a))
* **player:** let the player who reached a tied score first win ([0cc4819](https://github.com/OneLiteFeatherNET/Bounce/commit/0cc4819f49e63cf053c4f0bccd91c662aec17e42))

## [0.7.0](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.6.0...v0.7.0) (2026-10-10)


### Features

* add LuckPerms support ([#106](https://github.com/OneLiteFeatherNET/Bounce/issues/106)) ([563f066](https://github.com/OneLiteFeatherNET/Bounce/commit/563f066d31471ec5ea407651b60cc86e017efdd4))
* add powerups ([#134](https://github.com/OneLiteFeatherNET/Bounce/issues/134)) ([a583aef](https://github.com/OneLiteFeatherNET/Bounce/commit/a583aef00b790b31fce80f687575e77fc56d48df))
* make playground more randomly ([#105](https://github.com/OneLiteFeatherNET/Bounce/issues/105)) ([5adf11e](https://github.com/OneLiteFeatherNET/Bounce/commit/5adf11efe88dd1b4328d1e62b62220a7fea37f23))
* **setup:** add pica dependency ([#109](https://github.com/OneLiteFeatherNET/Bounce/issues/109)) ([06e9f19](https://github.com/OneLiteFeatherNET/Bounce/commit/06e9f19884221dc615d5b96b7366fb4350ea5f9c))


### Bug Fixes

* **config:** set default game time to five minutes ([6ec03ce](https://github.com/OneLiteFeatherNET/Bounce/commit/6ec03ce0295f0ef35872401521c4c3175483d693))
* **damage:** reduce invulnerability after a hit to one second ([bffa9b8](https://github.com/OneLiteFeatherNET/Bounce/commit/bffa9b896e996da7af6bad19e307fe9dfdcd2af4))
* **deps:** update dependency com.google.guava:guava to v33.7.2-android ([#130](https://github.com/OneLiteFeatherNET/Bounce/issues/130)) ([f8465c1](https://github.com/OneLiteFeatherNET/Bounce/commit/f8465c1c428b7c72da60caf8ded897536ca13b17))
* **deps:** update dependency com.google.guava:guava to v33.7.2-jre ([#131](https://github.com/OneLiteFeatherNET/Bounce/issues/131)) ([9bf15f8](https://github.com/OneLiteFeatherNET/Bounce/commit/9bf15f847bc1e4f9dc325b681e9c2b665bb42f75))
* **deps:** update dependency eu.cloudnetservice.cloudnet:bom to v4.0.0-rc18-snapshot ([#98](https://github.com/OneLiteFeatherNET/Bounce/issues/98)) ([6c19caf](https://github.com/OneLiteFeatherNET/Bounce/commit/6c19caf270cb05742691050796c9388c6f5c7999))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.5 ([#115](https://github.com/OneLiteFeatherNET/Bounce/issues/115)) ([6a502cd](https://github.com/OneLiteFeatherNET/Bounce/commit/6a502cdd48df671cbf20c2f00705294d70186812))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.6 ([#116](https://github.com/OneLiteFeatherNET/Bounce/issues/116)) ([a3e36d0](https://github.com/OneLiteFeatherNET/Bounce/commit/a3e36d048dda8d3098d1f8c525f0c1b436d1c3b0))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.7 ([#127](https://github.com/OneLiteFeatherNET/Bounce/issues/127)) ([0b3921a](https://github.com/OneLiteFeatherNET/Bounce/commit/0b3921ae123a445687e8467235a0d97172eac781))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.8 ([#133](https://github.com/OneLiteFeatherNET/Bounce/issues/133)) ([9c75e7c](https://github.com/OneLiteFeatherNET/Bounce/commit/9c75e7ca70434afb2b5a9a3c964aaf237cc18d8e))
* **deps:** update dependency net.onelitefeather:falco-bom to v3 ([#126](https://github.com/OneLiteFeatherNET/Bounce/issues/126)) ([5901acc](https://github.com/OneLiteFeatherNET/Bounce/commit/5901accf1236d29599833e6ba5624af40f6eed36))
* **deps:** update dependency net.onelitefeather:minestom-extensions-bom to v2.2.0 ([#113](https://github.com/OneLiteFeatherNET/Bounce/issues/113)) ([d0a51db](https://github.com/OneLiteFeatherNET/Bounce/commit/d0a51db43677c7e61aa92a3f6eb22312dc321ab9))
* **deps:** update dependency net.onelitefeather:pica to v0.1.1 ([#110](https://github.com/OneLiteFeatherNET/Bounce/issues/110)) ([d1b1519](https://github.com/OneLiteFeatherNET/Bounce/commit/d1b151987b4d65025258a9d0c5a56cf2c82b7e02))
* **deps:** update dependency net.onelitefeather:pica to v0.1.2 ([#111](https://github.com/OneLiteFeatherNET/Bounce/issues/111)) ([bed5744](https://github.com/OneLiteFeatherNET/Bounce/commit/bed57449dc145c876884af2d63e22e826df304a2))
* **deps:** update dependency net.onelitefeather:pica to v0.1.3 ([#114](https://github.com/OneLiteFeatherNET/Bounce/issues/114)) ([d4fc58f](https://github.com/OneLiteFeatherNET/Bounce/commit/d4fc58f2c5986571195970f98c206961d027512c))
* **deps:** update slf4j to v2.0.19 ([#117](https://github.com/OneLiteFeatherNET/Bounce/issues/117)) ([ef88acd](https://github.com/OneLiteFeatherNET/Bounce/commit/ef88acdabeabe67d4f9c647d6d20d32b6b3a9222))
* **deps:** update slf4j to v2.0.20 ([#128](https://github.com/OneLiteFeatherNET/Bounce/issues/128)) ([40dce72](https://github.com/OneLiteFeatherNET/Bounce/commit/40dce720a3fc9d6a74662b3c8ac6e76f7c0553f6))
* **inventory:** improve update handling ([98196dc](https://github.com/OneLiteFeatherNET/Bounce/commit/98196dc9fff05167801081eb89560a001d4f1213))
* **message:** avoid double prefix usage ([41ae0a2](https://github.com/OneLiteFeatherNET/Bounce/commit/41ae0a20bfa554dbfdb3f99c18960b9d6f14ad53))
* **message:** avoid some small typos ([707ab80](https://github.com/OneLiteFeatherNET/Bounce/commit/707ab809827c43e10db3af69a31052cb1640f55e))
* **phase:** end round when players leave during teleport ([#136](https://github.com/OneLiteFeatherNET/Bounce/issues/136)) ([4f4f21b](https://github.com/OneLiteFeatherNET/Bounce/commit/4f4f21b6c8d47cac4d94e471b4a288cea287c3df))
* **phase:** improve player left handling ([554e6f7](https://github.com/OneLiteFeatherNET/Bounce/commit/554e6f77f2e866b44cdc677ab168ea375e997f8a))
* **phase:** remove -1 from minPlayers check ([4674106](https://github.com/OneLiteFeatherNET/Bounce/commit/46741066bb6477068fb5ae248d40c5f44cf68eba))
* **phase:** use the configured game time ([212eb9c](https://github.com/OneLiteFeatherNET/Bounce/commit/212eb9c14f9fa8edf0dd9aeea5572b261714e12a))
* **player:** expire the last damager after five seconds ([78219bb](https://github.com/OneLiteFeatherNET/Bounce/commit/78219bb10f17f2011e06e8c03dab710b83a121e6))
* **player:** improve lava detection to avoid getting stuck in it ([#135](https://github.com/OneLiteFeatherNET/Bounce/issues/135)) ([4ce2c21](https://github.com/OneLiteFeatherNET/Bounce/commit/4ce2c21594b9f18ec537425dd60ad8b94d2d2c9b))
* **player:** prevent negative scores ([2308732](https://github.com/OneLiteFeatherNET/Bounce/commit/23087321ddd14aa640f5505502c04b9ca0c30531))
* **powerup:** remove blooper blindness on round end ([d38c34c](https://github.com/OneLiteFeatherNET/Bounce/commit/d38c34c875a394c6e76fe6d97d7207ccef89e4d0))
* **powerup:** remove blooper blindness when the round ends ([#137](https://github.com/OneLiteFeatherNET/Bounce/issues/137)) ([d38c34c](https://github.com/OneLiteFeatherNET/Bounce/commit/d38c34c875a394c6e76fe6d97d7207ccef89e4d0))
* **profile:** adjust profile handling at the end of the game ([#97](https://github.com/OneLiteFeatherNET/Bounce/issues/97)) ([71eaf9b](https://github.com/OneLiteFeatherNET/Bounce/commit/71eaf9bfed037a81511d9361ad709ac65ec8cb88))
* **push:** remove static usage ([981f97f](https://github.com/OneLiteFeatherNET/Bounce/commit/981f97f6cf1c805e53db3dec7df41bc9d55ebaae))

## [0.6.0](https://github.com/OneLiteFeatherNET/Bounce/compare/v0.5.0...v0.6.0) (2026-08-04)


### Features

* **ci:** add release please workflow ([#84](https://github.com/OneLiteFeatherNET/Bounce/issues/84)) ([4004533](https://github.com/OneLiteFeatherNET/Bounce/commit/400453312d6d0faf926ca42ada0242bf0c6cc159))


### Bug Fixes

* **deps:** update aonyx to v0.7.3 ([#77](https://github.com/OneLiteFeatherNET/Bounce/issues/77)) ([76b1849](https://github.com/OneLiteFeatherNET/Bounce/commit/76b18495a1e3de1325b73443980a1ebe8a963b2a))
* **deps:** update aonyx to v0.8.0 ([#78](https://github.com/OneLiteFeatherNET/Bounce/issues/78)) ([5a3cee1](https://github.com/OneLiteFeatherNET/Bounce/commit/5a3cee19c638fbd59fc3f7e4aa751fe0500e389e))
* **deps:** update dependency eu.cloudnetservice.cloudnet:bom to v4.0.0-rc17-snapshot ([#53](https://github.com/OneLiteFeatherNET/Bounce/issues/53)) ([e9e7eed](https://github.com/OneLiteFeatherNET/Bounce/commit/e9e7eed470f58ded1979302e9edaf55787c6fcf1))
* **deps:** update dependency io.github.togar2:minestompvp to v2026 ([#73](https://github.com/OneLiteFeatherNET/Bounce/issues/73)) ([35991d6](https://github.com/OneLiteFeatherNET/Bounce/commit/35991d64f8ce36f7c13c77b61660012815f08de7))
* **deps:** update dependency io.github.togar2:minestompvp to v2026.05.30-26.1.1 ([#75](https://github.com/OneLiteFeatherNET/Bounce/issues/75)) ([0e40884](https://github.com/OneLiteFeatherNET/Bounce/commit/0e4088464b6a1e7e87f2521a1d5177f269755f02))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.7.0 ([#55](https://github.com/OneLiteFeatherNET/Bounce/issues/55)) ([6a76092](https://github.com/OneLiteFeatherNET/Bounce/commit/6a76092937f5051d3e05aed875dda8822dcce32d))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.7.1 ([#66](https://github.com/OneLiteFeatherNET/Bounce/issues/66)) ([eb8a886](https://github.com/OneLiteFeatherNET/Bounce/commit/eb8a8869d56378d0eedea1cab13cf9b4913dd561))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.1 ([#89](https://github.com/OneLiteFeatherNET/Bounce/issues/89)) ([22d1559](https://github.com/OneLiteFeatherNET/Bounce/commit/22d15597b0f5fd098e9f5e68dd1470f7d11955ea))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.2 ([#90](https://github.com/OneLiteFeatherNET/Bounce/issues/90)) ([bfc2c0f](https://github.com/OneLiteFeatherNET/Bounce/commit/bfc2c0ffd8f28a1ca82b3ddd8ba11a8dacef0595))
* **deps:** update dependency net.onelitefeather:aonyx-bom to v0.8.4 ([#91](https://github.com/OneLiteFeatherNET/Bounce/issues/91)) ([ee53eef](https://github.com/OneLiteFeatherNET/Bounce/commit/ee53eefec968a9d5f50d479eaedb5b4b2a80a765))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.1 ([#56](https://github.com/OneLiteFeatherNET/Bounce/issues/56)) ([0d5951e](https://github.com/OneLiteFeatherNET/Bounce/commit/0d5951e907ba36e74ce420c5425aa8b42fefa53f))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.2 ([#57](https://github.com/OneLiteFeatherNET/Bounce/issues/57)) ([03f5c38](https://github.com/OneLiteFeatherNET/Bounce/commit/03f5c3842b1452441ba02d2f9651462f381145f2))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.3 ([#60](https://github.com/OneLiteFeatherNET/Bounce/issues/60)) ([6ebe214](https://github.com/OneLiteFeatherNET/Bounce/commit/6ebe21483f56c9463d7a94fdb7a83f1ec10668ee))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.4 ([#64](https://github.com/OneLiteFeatherNET/Bounce/issues/64)) ([0cae41d](https://github.com/OneLiteFeatherNET/Bounce/commit/0cae41df82e77c86fbddca2c23b8b23d31fd3a26))
* **deps:** update dependency org.slf4j:slf4j-simple to v2.0.18 ([#72](https://github.com/OneLiteFeatherNET/Bounce/issues/72)) ([83ea131](https://github.com/OneLiteFeatherNET/Bounce/commit/83ea1317c4f23f295573830ebbb1126c17fc9cad))
* **setup:** update constructor usage ([807108f](https://github.com/OneLiteFeatherNET/Bounce/commit/807108f883a5b21b98dbaefc8e99484b372edf6a))
