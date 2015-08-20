angular.module('wtpDurations', [])
    .controller('WtpDurationsController', ['$scope', '$http', function($scope, $http) {
        $scope.platforms = [{
                label: 'NES',
                value: 'NES'
            }, {
                label: 'SNES',
                value: 'SNES'
            }, {
                label: 'Nintendo 64',
                value: 'N64'
            }, {
                label: 'GameCube',
                value: 'GC'
            }, {
                label: 'Wii',
                value: 'WII'
            }, {
                label: 'Wii U',
                value: 'WIIU'
            }, {
                label: 'Gameboy',
                value: 'GB'
            }, {
                label: 'Gameboy Color',
                value: 'GBC'
            }, {
                label: 'Gameboy Advance',
                value: 'GBA'
            }, {
                label: '3DS',
                value: '3DS'
            }, {
                label: 'PlayStation',
                value: 'PSX'
            }, {
                label: 'PlayStation 2',
                value: 'PS2'
            }, {
                label: 'PlayStation 3',
                value: 'PS3'
            }, {
                label: 'PlayStation 4',
                value: 'PS4'
            }, {
                label: 'PlayStation Portable',
                value: 'PSP'
            }, {
                label: 'PlayStation Vita',
                value: 'PSVITA'
            }, {
                label: 'Master System',
                value: 'MS'
            }, {
                label: 'Genesis/Mega Drive',
                value: 'MD'
            }, {
                label: 'Saturn',
                value: 'SS'
            }, {
                label: 'Dreamcast',
                value: 'DC'
            }, {
                label: 'Game gear',
                value: 'GG'
            }, {
                label: 'DOS',
                value: 'DOS'
            }, {
                label: 'Windows Pre-2000',
                value: 'Windows Pos-2000'
            }
        ];

        $http.get('data/games-duration.json').
        then(function(response) {
            $scope.fullSet = response.data;
        }, function(response) {
            // Do nothing
        });

        $scope.$watchGroup(['currPlatform', 'minHours', 'maxHours'], function() {
            function shouldAddGame(game) {
                var result = true;

                if (game.platform !== $scope.currPlatform) {
                    result = false;
                } else if (game.duration < $scope.minHours) {
                    result = false;
                } else if (game.duration > $scope.maxHours) {
                    result = false;
                }

                return result;
            };

            if ($scope.currPlatform) {
                $scope.games = [];
                $scope.fullSet.forEach(function(game) {
                    if (shouldAddGame(game)) {
                        $scope.games.push(game);
                    }
                });
            }
        });

    }]);
