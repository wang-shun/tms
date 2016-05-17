/**
 * Created by 16030117 on 2016/4/14.
 */
define(function (require, exports, module) {
    var $ = require("jquery");
    function loadProvinces(provinceIds) {
        $.each(areaLists, function (k, p) {
            var option = "<option value='" + p.name + "'>" + p.name + "</option>";
            $.each(provinceIds, function(k, p){
                $("#".concat(p)).append(option);
            });
        });
    }

    function loadCities(provinceName, cityId, countyId){
        var cities = getCities(provinceName);
        $("#".concat(cityId).concat(" option")).remove();
        $("#".concat(countyId).concat(" option")).remove();
        $("#".concat(countyId)).append("<option value=''>请选择</option>");
        if(cities.length>0){
            $.each(cities, function (k, p) {
                var option = "<option value='" + p.name + "'>" + p.name + "</option>";
                $("#".concat(cityId)).append(option);
            });
            var firstCity = cities[0];
            var counties = firstCity.areaList;
            $.each(counties, function (k, p) {
                var option = "<option value='" + p + "'>" + p + "</option>";
                $("#".concat(countyId)).append(option);
            });
        }else{
            $("#".concat(cityId)).append("<option value=''>请选择</option>");
        }
    }

    function getCities(provinceName){
        var cities = [];
        $.each(areaLists, function(k,p){
            if(provinceName == p.name){
                cities = p.cityList;
                return;
            }
        });
        return cities;
    }

    function loadCounties(provinceName, cityName, countyId){
        $("#".concat(countyId).concat(" option")).remove();
        var counties = areaSelection.getCounties(province, city);
        if(counties.length>0){
            $.each(counties, function (k, p) {
                var option = "<option value='" + p + "'>" + p + "</option>";
                $("#".concat(countyId)).append(option);
            });
        }else{
            $("#".concat(countyId)).append("<option value=''>请选择</option>");
        }
    }

    function getCounties(provinceName, cityName){
        var counties = [];
        $.each(areaLists, function(k,p){
            if(provinceName == p.name){
                var cities = p.cityList;
                $.each(cities, function(k,p){
                    if(cityName == p.name){
                        counties = p.areaList;
                        return;
                    }
                });
            }
        });
        return counties;
    }

    var areaLists = [
        {
            name: '上海', cityList: [
            {
                name: '上海',
                areaList: ['黄浦区', '卢湾区', '徐汇区', '长宁区', '静安区', '普陀区', '闸北区', '虹口区', '杨浦区', '闵行区', '宝山区', '嘉定区', '浦东新区', '金山区', '松江区', '青浦区', '南汇区', '奉贤区', '崇明县']
            }
        ]
        }/*,
        {
            name: '江苏', cityList: [
            {
                name: '南京市',
                areaList: ['玄武区', '白下区', '秦淮区', '建邺区', '鼓楼区', '下关区', '浦口区', '栖霞区', '雨花台区', '江宁区', '六合区', '溧水县', '高淳县']
            },
            {name: '无锡市', areaList: ['崇安区', '南长区', '北塘区', '锡山区', '惠山区', '滨湖区', '江阴市', '宜兴市']},
            {
                name: '徐州市',
                areaList: ['鼓楼区', '云龙区', '九里区', '贾汪区', '泉山区', '丰　县', '沛　县', '铜山县', '睢宁县', '新沂市', '邳州市']
            },
            {name: '常州市', areaList: ['天宁区', '钟楼区', '戚墅堰区', '新北区', '武进区', '溧阳市', '金坛市']},
            {
                name: '苏州市',
                areaList: ['沧浪区', '平江区', '金阊区', '虎丘区', '吴中区', '相城区', '常熟市', '张家港市', '昆山市', '吴江市', '太仓市']
            },
            {name: '南通市', areaList: ['崇川区', '港闸区', '海安县', '如东县', '启东市', '如皋市', '通州市', '海门市']},
            {name: '连云港市', areaList: ['连云区', '新浦区', '海州区', '赣榆县', '东海县', '灌云县', '灌南县']},
            {name: '淮安市', areaList: ['清河区', '楚州区', '淮阴区', '清浦区', '涟水县', '洪泽县', '盱眙县', '金湖县']},
            {name: '盐城市', areaList: ['亭湖区', '盐都区', '响水县', '滨海县', '阜宁县', '射阳县', '建湖县', '东台市', '大丰市']},
            {name: '扬州市', areaList: ['广陵区', '邗江区', '郊　区', '宝应县', '仪征市', '高邮市', '江都市']},
            {name: '镇江市', areaList: ['京口区', '润州区', '丹徒区', '丹阳市', '扬中市', '句容市']},
            {name: '泰州市', areaList: ['海陵区', '高港区', '兴化市', '靖江市', '泰兴市', '姜堰市']},
            {name: '宿迁市', areaList: ['宿城区', '宿豫区', '沭阳县', '泗阳县', '泗洪县']}
        ]
        },
        {
            name: '浙江', cityList: [
            {
                name: '杭州市',
                areaList: ['上城区', '下城区', '江干区', '拱墅区', '西湖区', '滨江区', '萧山区', '余杭区', '桐庐县', '淳安县', '建德市', '富阳市', '临安市']
            },
            {
                name: '宁波市',
                areaList: ['海曙区', '江东区', '江北区', '北仑区', '镇海区', '鄞州区', '象山县', '宁海县', '余姚市', '慈溪市', '奉化市']
            },
            {
                name: '温州市',
                areaList: ['鹿城区', '龙湾区', '瓯海区', '洞头县', '永嘉县', '平阳县', '苍南县', '文成县', '泰顺县', '瑞安市', '乐清市']
            },
            {name: '嘉兴市', areaList: ['秀城区', '秀洲区', '嘉善县', '海盐县', '海宁市', '平湖市', '桐乡市']},
            {name: '湖州市', areaList: ['吴兴区', '南浔区', '德清县', '长兴县', '安吉县']},
            {name: '绍兴市', areaList: ['越城区', '绍兴县', '新昌县', '诸暨市', '上虞市', '嵊州市']},
            {name: '金华市', areaList: ['婺城区', '金东区', '武义县', '浦江县', '磐安县', '兰溪市', '义乌市', '东阳市', '永康市']},
            {name: '衢州市', areaList: ['柯城区', '衢江区', '常山县', '开化县', '龙游县', '江山市']},
            {name: '舟山市', areaList: ['定海区', '普陀区', '岱山县', '嵊泗县']},
            {name: '台州市', areaList: ['椒江区', '黄岩区', '路桥区', '玉环县', '三门县', '天台县', '仙居县', '温岭市', '临海市']},
            {name: '丽水市', areaList: ['莲都区', '青田县', '缙云县', '遂昌县', '松阳县', '云和县', '庆元县', '景宁畲族自治县', '龙泉市']}
        ]
        }*/
    ];

    exports.loadProvinces = loadProvinces;
    exports.loadCities = loadCities;
    exports.loadCounties = loadCounties;
});


